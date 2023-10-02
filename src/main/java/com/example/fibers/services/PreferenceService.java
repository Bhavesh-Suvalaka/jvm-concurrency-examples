package com.example.fibers.services;

import com.example.fibers.models.CustomerPreference;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PreferenceService {
    public List<CustomerPreference> fetchCustomerPreferences(String customerId) {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
    }
}
