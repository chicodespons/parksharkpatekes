package com.switchfully.patekes.parksharkpatekes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ParkingAllocation {
    @Id
    @SequenceGenerator(name="parking_allocation_seq", sequenceName = "parking_allocation_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_allocation_seq")
    private long id;
    @OneToOne
    private Member member;
    @OneToOne
    private ParkingLot parkingLot;
    private boolean active;
    private LocalDateTime start_time;
    private LocalDateTime stop_time;
    @OneToOne(cascade = CascadeType.ALL)
    private LicensePlate licensePlate;

    public ParkingAllocation(Member member, ParkingLot parkingLot, boolean active, LocalDateTime start_time, LocalDateTime stop_time, LicensePlate licensePlate) {
        this.member = member;
        this.parkingLot = parkingLot;
        this.active = active;
        this.start_time = start_time;
        this.stop_time = stop_time;
        this.licensePlate = licensePlate;
    }
}
