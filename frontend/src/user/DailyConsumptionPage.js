import React, { useState } from "react";
import api from "../api/client";
import {
    LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid
} from "recharts";
import "../index.css";

export default function ConsumptionPage() {

    const deviceId = "8c8b5480-0b0e-4e1f-931c-8fd1b4ff008c";

    const [date, setDate] = useState("");
    const [data, setData] = useState([]);
    const [error, setError] = useState("");

    const fetchData = async () => {
        if (!date) return;

        try {
            const res = await api.get(`/monitoring/device/${deviceId}/day/${date}`);

            const formatted = res.data
                .map(entry => ({
                    hour: entry.hour,
                    consumption: entry.consumption
                }))
                .sort((a, b) => a.hour - b.hour);

            setData(formatted);
            setError("");
        } catch {
            setError("No data available for this date.");
            setData([]);
        }
    };

    return (
        <div className="page-center">
            <div className="card large-card">
                <h2 className="title">Daily Energy Consumption</h2>

                <div style={{ textAlign: "center", marginBottom: "20px" }}>
                    <input
                        type="date"
                        className="input"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        style={{ width: "220px" }}
                    />
                    <button className="button" style={{ marginLeft: "10px" }} onClick={fetchData}>
                        Show Chart
                    </button>
                </div>

                {error && <p className="error">{error}</p>}

                {data.length > 0 && (
                    <div style={{ height: "400px", width: "100%" }}>
                        <ResponsiveContainer>
                            <LineChart data={data}>
                                <CartesianGrid stroke="#ffb6c1" strokeDasharray="4 4" />
                                <XAxis
                                    dataKey="hour"
                                    tickFormatter={(h) => `${String(h).padStart(2, "0")}:00`}
                                    stroke="#ff69b4"
                                />
                                <YAxis stroke="#ff69b4" />
                                <Tooltip
                                    formatter={(value) => [`${value} kWh`, "Consumption"]}
                                    labelFormatter={(h) => `Hour: ${h}:00`}
                                />
                                <Line
                                    type="monotone"
                                    dataKey="consumption"
                                    stroke="#ff69b4"
                                    strokeWidth={3}
                                    dot={{ r: 4, fill: "#ff69b4" }}
                                />
                            </LineChart>
                        </ResponsiveContainer>
                    </div>
                )}
            </div>
        </div>
    );
}
