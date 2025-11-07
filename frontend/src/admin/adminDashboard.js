import React, { useEffect, useState } from "react";
import api from "../api/client";
import "../index.css";

export default function AdminDashboard() {
    const [users, setUsers] = useState([]);
    const [devices, setDevices] = useState([]);
    const [newUser, setNewUser] = useState({ username: "", role: "CLIENT", password: "1234" });
    const [newDevice, setNewDevice] = useState({ name: "", maxConsumption: 0 });
    const [assignUser, setAssignUser] = useState("");
    const [assignDevices, setAssignDevices] = useState([]);

    const loadAll = async () => {
        const u = await api.get("/users");
        const d = await api.get("/devices");
        setUsers(u.data);
        setDevices(d.data);
    };

    useEffect(() => { loadAll(); }, []);

    const addUser = async () => {
        await api.post("/users", newUser);
        setNewUser({ username: "", role: "CLIENT", password: "1234" });
        loadAll();
    };

    const addDevice = async () => {
        await api.post("/devices", newDevice);
        setNewDevice({ name: "", maxConsumption: 0 });
        loadAll();
    };

    const deleteUser = async (id) => { await api.delete(`/users/${id}`); loadAll(); };
    const deleteDevice = async (id) => { await api.delete(`/devices/${id}`); loadAll(); };

    const assign = async () => {
        await api.post(`/users/${assignUser}/devices`, { deviceIds: assignDevices });
        alert("Assigned successfully!");
    };

    return (
        <div className="page-center">
            <div className="card large-card">
                <h2 className="title">Admin Dashboard 💼</h2>

                <section>
                    <h3>Users</h3>
                    <ul>
                        {users.map(u => (
                            <li key={u.id}>
                                {u.username} ({u.role})
                                <button className="small-btn" onClick={() => deleteUser(u.id)}>❌</button>
                            </li>
                        ))}
                    </ul>
                    <input className="input" placeholder="Username" value={newUser.username}
                           onChange={e => setNewUser({...newUser, username:e.target.value})}/>
                    <select className="input" value={newUser.role}
                            onChange={e => setNewUser({...newUser, role:e.target.value})}>
                        <option value="CLIENT">Client</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                    <button className="button" onClick={addUser}>Add User</button>
                </section>

                <hr/>

                <section>
                    <h3>Devices</h3>
                    <ul>
                        {devices.map(d => (
                            <li key={d.id}>
                                {d.name} – {d.maxConsumption}W
                                <button className="small-btn" onClick={() => deleteDevice(d.id)}>❌</button>
                            </li>
                        ))}
                    </ul>
                    <input className="input" placeholder="Device name" value={newDevice.name}
                           onChange={e => setNewDevice({...newDevice, name:e.target.value})}/>
                    <input className="input" type="number" placeholder="Max consumption"
                           value={newDevice.maxConsumption}
                           onChange={e => setNewDevice({...newDevice, maxConsumption:e.target.value})}/>
                    <button className="button" onClick={addDevice}>Add Device</button>
                </section>

                <hr/>

                <section>
                    <h3>Assign Devices</h3>
                    <select className="input" value={assignUser} onChange={e=>setAssignUser(e.target.value)}>
                        <option value="">Select User</option>
                        {users.map(u=> <option key={u.id} value={u.id}>{u.username}</option>)}
                    </select>

                    <div className="checkbox-list">
                        {devices.map(d => (
                            <label key={d.id}>
                                <input type="checkbox"
                                       checked={assignDevices.includes(d.id)}
                                       onChange={e => {
                                           if (e.target.checked) setAssignDevices([...assignDevices, d.id]);
                                           else setAssignDevices(assignDevices.filter(x=>x!==d.id));
                                       }}/>
                                {d.name}
                            </label>
                        ))}
                    </div>
                    <button className="button" disabled={!assignUser} onClick={assign}>Assign</button>
                </section>
            </div>
        </div>
    );
}
