package com.vadhvan.repositories;

import com.vadhvan.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    List<Expense> findByVehicleId(String vehicleId);
    List<Expense> findByTripId(String tripId);
}
