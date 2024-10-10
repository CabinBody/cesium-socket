package com.example.getposition.controller;

import com.example.getposition.entity.GetPosition;
import com.example.getposition.mapper.GetPositionMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;;


@RestController
@RequestMapping("/GetPosition")
public class GetPositionController {

    @Resource
    GetPositionMapper getPositionMapper;

    @CrossOrigin(origins = "*" , maxAge = 3600)
    @GetMapping("/select")
    public List<GetPosition> findGetPosition() {
        return getPositionMapper.GetPositionALl();
    }
}
