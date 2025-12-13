import React, { useEffect, useState } from "react";
import api from "../api/client";
import "../index.css";
import DeviceTable from "../components/DeviceTable";
import UserChatPopup from "./UserChatPopup";

export default function UserDashboard() {
    const [devices, setDevices] = useState([]);

    useEffect(() => {
        api.get("/device/me")
            .then(res => setDevices(res.data))
            .catch(console.error);
    }, []);

    return (
        <div className="page-center">
            <div className="card large-card">
                <h2 className="title">My Devices</h2>
                <a href="/user/energy" className="button" style={{ display: "inline-block", textAlign: "center" }}>
                    View Historical Energy Consumption
                </a>
                {devices.length === 0 ? (
                    <p className="text">No devices registered yet.</p>
                ) : (
                    <DeviceTable devices={devices} />
                )}
            </div>

            <UserChatPopup />

        </div>
    );
}
