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
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "stop_time")
    private LocalDateTime stopTime;
    @OneToOne
    private LicensePlate licensePlate;

    public ParkingAllocation(Member member, ParkingLot parkingLot, LicensePlate licensePlate) {
        this.member = member;
        this.parkingLot = parkingLot;
        active = true;
        startTime = LocalDateTime.now();
        this.licensePlate = licensePlate;
        updatePresentParkingLotCapacity();
    }

    private void updatePresentParkingLotCapacity() {
        parkingLot.setPresentCapacity(parkingLot.getPresentCapacity() + 1);
    }
}
