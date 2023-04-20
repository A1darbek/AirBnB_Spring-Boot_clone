package com.ayderbek.springbootexample.rest;

import com.ayderbek.springbootexample.domain.Host;
import com.ayderbek.springbootexample.domain.Property;
import com.ayderbek.springbootexample.ports.HostService;
import com.ayderbek.springbootexample.request.HostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/hosts")
@CrossOrigin("*")
public class HostController {
    private final HostService hostService;


    @GetMapping
    public List<Host> getAllHosts() {
        var hosts = hostService.getAllHosts();
        return hosts;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createHost(@RequestBody HostRequest hostRequest) {
           var host =  hostService.createHost(hostRequest);
            return new ResponseEntity<>("Host created successfully", HttpStatus.CREATED);

    }

    @GetMapping("/most-active")
    public List<Host> getMostActiveHosts() {
        return hostService.findMostActiveHosts();
    }
}
