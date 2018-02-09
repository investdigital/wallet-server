package com.oxchains.wallet.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by huohuo on 2018/1/29.
 */
@Entity
@Data
public class TouchInfo {
    @Id
    private String id;
    @NotNull(message = "faild: address is null")
    private String address; //用户地址
    @NotNull(message = "faild: IMEI is null")
    private String imei; //设备编号
    @NotNull(message = "faild: Device type is null")
    private String type; //设备类型 Android Ios
    @NotNull(message = "faild: RegistrationID  is null")
    private String RegistrationID;
 }
