import React, { useEffect, useState } from "react";
import api from "../api/client";
import "../index.css";

export default function UserDashboard() {
    const [devices, setDevices] = useState([]);

    useEffect(() => {
        api.get("/device").then(res => setDevices(res.data));
    }, []);

    return (
        <div className="page-center">
            <div className="card large-card">
                <h2 className="title">My Devices</h2>
                <ul>
                    {devices.map(d => (
                        <li key={d.id}>{d.name} {d.maximumConsumption} {d.yearOfManufacture}W</li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
