/*
 It's a playground for testing things :)
 */
package com.gelileo.crud.playground;

import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/playground")
public class PlaygroundController {
    private final SystemUserRepository systemUserRepository;
    @GetMapping("/")
    public ResponseEntity<Collection<SystemUser>> findAll() {
        return ResponseEntity.ok(systemUserRepository.findAll());
    }

}
