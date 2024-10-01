/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package helloworld;

import com.alibaba.fastjson2.annotation.JSONField;

public class Vehicle {

    @JSONField(name = "vehicle-type")
    private String vehicleType;

    @JSONField(name = "vehicleID")
    private String vehicleId;

    public Vehicle() {
    }

    public Vehicle(String vehicleType, String vehicleId) {
        this.vehicleType = vehicleType;
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleType='" + vehicleType + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                '}';
    }
}
