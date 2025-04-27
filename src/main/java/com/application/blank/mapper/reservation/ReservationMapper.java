package com.application.blank.mapper.reservation;

import com.application.blank.dto.reservation.PaymentsDTO;
import com.application.blank.dto.reservation.ReservationDTO;
import com.application.blank.entity.client.Client;
import com.application.blank.entity.reservation.Payments;
import com.application.blank.entity.reservation.Reservation;
import com.application.blank.entity.room.Prices;
import com.application.blank.entity.room.Room;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.repository.client.ClientRepository;
import com.application.blank.repository.room.PricesRepository;
import com.application.blank.repository.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    @Autowired private ClientRepository clientRepository;
    @Autowired private RoomRepository   roomRepository;
    @Autowired private PricesRepository pricesRepository;

    public ReservationDTO toDTO(Reservation r) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(r.getReservationId());
        dto.setClient(r.getClient().getClientId());
        dto.setRoom(r.getRoom().getRoomId());
        dto.setPrice(r.getPrice().getPriceId());
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

    public Reservation toEntity(ReservationDTO dto) throws ResourceNotFoundException {
        Client client = clientRepository.findById(dto.getClient())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for ID: " + dto.getClient()));
        Room room = roomRepository.findById(dto.getRoom())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + dto.getRoom()));
        Prices priceEnt = pricesRepository.findById(dto.getPrice())
                .orElseThrow(() -> new ResourceNotFoundException("Price not found for ID: " + dto.getPrice()));

        Reservation res = new Reservation();
        res.setClient(client);
        res.setRoom(room);
        res.setPrice(priceEnt);
        res.setEnded(false);

        int periods = dto.getReservationDaysAmount();
        int daysPerPrice = priceEnt.getDaysAmount();
        int totalDays = periods * daysPerPrice;
        res.setReservationDaysAmount(totalDays);

        LocalDateTime start = LocalDateTime.now();
        res.setStartDate(start);
        res.setEndDate(start.plusDays(totalDays));

        res.setDeposit(priceEnt.getDeposit());
        BigDecimal subtotal = priceEnt.getPrice().multiply(BigDecimal.valueOf(periods));
        res.setTotal(subtotal.add(priceEnt.getDeposit()));

        List<Payments> pagos = new ArrayList<>();
        for (int i = 0; i < periods; i++) {
            Payments p = new Payments();
            p.setReservation(res);
            p.setAmount(priceEnt.getPrice());
            p.setPaid(false);
            pagos.add(p);
        }
        res.setPayments(pagos);

        return res;
    }
}
