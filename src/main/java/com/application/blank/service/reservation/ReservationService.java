package com.application.blank.service.reservation;

import com.application.blank.dto.reservation.PaymentsDTO;
import com.application.blank.dto.reservation.ReservationDTO;
import com.application.blank.entity.client.Client;
import com.application.blank.entity.reservation.Payments;
import com.application.blank.entity.reservation.Reservation;
import com.application.blank.entity.room.Prices;
import com.application.blank.entity.room.Room;
import com.application.blank.entity.room.RoomStatus;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.client.ClientRepository;
import com.application.blank.repository.reservation.ReservationRepository;
import com.application.blank.repository.room.PricesRepository;
import com.application.blank.repository.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private ClientRepository    clientRepository;
    @Autowired private RoomRepository      roomRepository;
    @Autowired private PricesRepository    pricesRepository;

    // Obtener todas las reservas
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener reserva por ID
    public ReservationDTO getReservationById(Long id) throws ResourceNotFoundException {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for ID: " + id));
        return mapToDTO(r);
    }

    // Crear nueva reserva
    @Transactional
    public ReservationDTO saveReservation(ReservationDTO dto) throws ResourceNotFoundException {
        Client client = clientRepository.findById(dto.getClient())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for ID: " + dto.getClient()));
        Room room = roomRepository.findById(dto.getRoom())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: "   + dto.getRoom()));
        Prices priceEnt = pricesRepository.findById(dto.getPrice())
                .orElseThrow(() -> new ResourceNotFoundException("Price not found for ID: "  + dto.getPrice()));

        if (room.getRoomStatus() != RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Room is not available for reservation.");
        }

        // 1) Construimos la entidad
        Reservation res = new Reservation();
        res.setClient(client);
        res.setRoom(room);
        res.setPrice(priceEnt);           // <-- asignamos la ENTIDAD Prices
        res.setEnded(false);

        // 2) Calculamos días y fechas
        int periods      = dto.getReservationDaysAmount();
        int daysPerPrice = priceEnt.getDaysAmount();
        int totalDays    = periods * daysPerPrice;
        res.setReservationDaysAmount(totalDays);

        LocalDateTime start = LocalDateTime.now();
        res.setStartDate(start);
        res.setEndDate(start.plusDays(totalDays));

        // 3) Depósito y total
        res.setDeposit(priceEnt.getDeposit());
        BigDecimal subtotal = priceEnt.getPrice().multiply(BigDecimal.valueOf(periods));
        res.setTotal(subtotal.add(priceEnt.getDeposit()));

        // 4) Generamos los pagos
        List<Payments> pagos = new ArrayList<>();
        for (int i = 0; i < periods; i++) {
            Payments p = new Payments();
            p.setReservation(res);
            p.setAmount(priceEnt.getPrice());
            p.setPaid(false);
            pagos.add(p);
        }
        res.setPayments(pagos);

        // 5) Marcamos habitación ocupada
        room.setRoomStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        // 6) Guardamos y devolvemos DTO
        Reservation saved = reservationRepository.save(res);
        return mapToDTO(saved);
    }

    // Actualizar una reserva existente
    @Transactional
    public ReservationDTO updateReservation(Long id, ReservationDTO dto) throws ResourceNotFoundException {
        Reservation existing = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for ID: " + id));

        // 1) Cambio de habitación…
        if (!existing.getRoom().getRoomId().equals(dto.getRoom())) {
            Room oldRoom = existing.getRoom();
            oldRoom.setRoomStatus(RoomStatus.AVAILABLE);
            roomRepository.save(oldRoom);

            Room newRoom = roomRepository.findById(dto.getRoom())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + dto.getRoom()));
            if (newRoom.getRoomStatus() != RoomStatus.AVAILABLE) {
                throw new IllegalStateException("New room is not available.");
            }
            newRoom.setRoomStatus(RoomStatus.OCCUPIED);
            roomRepository.save(newRoom);

            existing.setRoom(newRoom);
        }

        // 2) Cambio de cliente…
        if (!existing.getClient().getClientId().equals(dto.getClient())) {
            Client c = clientRepository.findById(dto.getClient())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found for ID: " + dto.getClient()));
            existing.setClient(c);
        }

        // 3) Recalcular price/días si cambió…
        boolean priceChanged  = !existing.getPrice().getPriceId().equals(dto.getPrice());
        int     oldPeriods    = existing.getReservationDaysAmount() / existing.getPrice().getDaysAmount();
        boolean periodChanged = oldPeriods != dto.getReservationDaysAmount();

        if (priceChanged || periodChanged) {
            Prices priceEnt = pricesRepository.findById(dto.getPrice())
                    .orElseThrow(() -> new ResourceNotFoundException("Price not found for ID: " + dto.getPrice()));

            int periods   = dto.getReservationDaysAmount();
            int totalDays = periods * priceEnt.getDaysAmount();
            existing.setReservationDaysAmount(totalDays);
            existing.setEndDate(existing.getStartDate().plusDays(totalDays));

            existing.setDeposit(priceEnt.getDeposit());
            existing.setPrice(priceEnt);

            // regenerar pagos
            existing.getPayments().clear();
            for (int i = 0; i < periods; i++) {
                Payments p = new Payments();
                p.setReservation(existing);
                p.setAmount(priceEnt.getPrice());
                p.setPaid(false);
                existing.getPayments().add(p);
            }

            BigDecimal subtotal = priceEnt.getPrice().multiply(BigDecimal.valueOf(periods));
            existing.setTotal(subtotal.add(priceEnt.getDeposit()));
        }

        // 4) Actualizar estado de cada pago según el DTO
        if (dto.getPayments() != null && !dto.getPayments().isEmpty()) {
            // Mapeamos paymentsId -> paid
            Map<Long, Boolean> pagosStatus = dto.getPayments().stream()
                    .collect(Collectors.toMap(PaymentsDTO::getPaymentsId, PaymentsDTO::isPaid));
            // Actualizamos cada Payments en la entidad
            existing.getPayments().forEach(p -> {
                Boolean nuevoEstado = pagosStatus.get(p.getPaymentsId());
                if (nuevoEstado != null) {
                    p.setPaid(nuevoEstado);
                }
                existing.setUpdatedAt(LocalDateTime.now());
            });
        }

        // 5) Cambiar estado de ended y estado de la habitación si corresponde
        if (dto.isEnded() != existing.isEnded()) {
            existing.setEnded(dto.isEnded());

            Room room = existing.getRoom();
            if (dto.isEnded()) {
                room.setRoomStatus(RoomStatus.UNDER_CLEANING);
            } else {
                room.setRoomStatus(RoomStatus.OCCUPIED);
            }
            roomRepository.save(room);
        }

        // Guardar; gracias al CascadeType.ALL se persisten también los cambios en Payments
        Reservation updated = reservationRepository.save(existing);
        return mapToDTO(updated);
    }

    // Eliminar reserva
    @Transactional
    public Map<String, Boolean> deleteReservation(Long id) throws ResourceNotFoundException {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found for ID: " + id));

        Room room = res.getRoom();
        room.setRoomStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);

        reservationRepository.delete(res);
        return Collections.singletonMap("deleted", Boolean.TRUE);
    }

    // Map Entity → DTO (extrae priceId de la entidad Prices)
    private ReservationDTO mapToDTO(Reservation r) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(r.getReservationId());
        dto.setClient(r.getClient().getClientId());
        dto.setRoom(r.getRoom().getRoomId());
        dto.setPrice(r.getPrice().getPriceId());            // <-- aquí
        dto.setStartDate(r.getStartDate());
        dto.setEndDate(r.getEndDate());
        dto.setReservationDaysAmount(r.getReservationDaysAmount());
        dto.setDeposit(r.getDeposit());
        dto.setTotal(r.getTotal());
        dto.setEnded(r.isEnded());
        dto.setCreateAt(r.getCreatedAt());
        dto.setUpdateAt(r.getUpdatedAt());
        dto.setPayments(r.getPayments().stream().map(p -> {
            PaymentsDTO pd = new PaymentsDTO();
            pd.setPaymentsId(p.getPaymentsId());
            pd.setReservation(p.getReservation().getReservationId());
            pd.setAmount(p.getAmount());
            pd.setPaid(p.isPaid());
            return pd;
        }).collect(Collectors.toList()));
        return dto;
    }
}
