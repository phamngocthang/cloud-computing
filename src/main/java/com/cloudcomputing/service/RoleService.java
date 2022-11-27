package com.cloudcomputing.service;

import com.cloudcomputing.entity.Role;
import com.cloudcomputing.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<String> getRoleNames(Integer userId) {
        return roleRepository.findAllByUserId(userId)
                .stream().map(Role::getName)
                .collect(Collectors.toList());
    }
}
