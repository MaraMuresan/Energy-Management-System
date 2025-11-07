import React, { useEffect, useState } from "react";
import api from "../api/client";
import "../index.css";

export default function AdminDashboard() {
    const [users, setUsers] = useState([]);
    const [devices, setDevices] = useState([]);
    const [newUser, setNewUser] = useState({ username: "", address: "", age: 0, password: "1234", role: "USER" });
    const [newDevice, setNewDevice] = useState({ name: "", maximumConsumption: 0.0, yearOfManufacture: 2010 });
    const [assignUser, setAssignUser] = useState("");
    const [assignDevices, setAssignDevices] = useState([]);

    const loadAll = async () => {
        const u = await api.get("/user");
        const d = await api.get("/device");
        setUsers(u.data);
        setDevices(d.data);
    };

    useEffect(() => { loadAll(); }, []);

    const addUser = async () => {
        await api.post("/user", newUser);
        setNewUser({ username: "", address: "", age: 0, password: "1234", role: "USER" });
        loadAll();
    };

    const addDevice = async () => {
        await api.post("/device", newDevice);
        setNewDevice({ name: "", maximumConsumption: 0.0, yearOfManufacture: 2010});
        loadAll();
    };

    const deleteUser = async (id) => { await api.delete(`/user/${id}`); loadAll(); };
    const deleteDevice = async (id) => { await api.delete(`/device/${id}`); loadAll(); };

    const assign = async () => {
        await api.post(`/user/${assignUser}/device`, { deviceIds: assignDevices });
        alert("Assigned successfully!");
    };

    return (
        <div className="page-center">
            <div className="card large-card">
                <h2 className="title">Admin Dashboard</h2>

                <section>
                    <h3>Users</h3>
                    <ul>
                        {users.map(u => (
                            <li key={u.id}>
                                {u.username} {u.address} {u.age} {u.password} ({u.role})
                                <button className="small-btn" onClick={() => deleteUser(u.id)}>delete</button>
                            </li>
                        ))}
                    </ul>
                    <input className="input" placeholder="Username" value={newUser.username} onChange={e => setNewUser({...newUser, username:e.target.value})}/>
                    <input className="input" placeholder="Address" value={newUser.address} onChange={e => setNewUser({...newUser, address:e.target.value})} />
                    <input className="input" placeholder="Age" value={newUser.age} onChange={e => setNewUser({...newUser, age:e.target.value})} />
                    <input className="input" placeholder="Password" value={newUser.password} onChange={e => setNewUser({...newUser, password:e.target.value})} />
                    <select className="input" value={newUser.role} onChange={e => setNewUser({...newUser, role:e.target.value})}>
                        <option value="USER">User</option>
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
                                {d.name} {d.maximumConsumption} {d.yearOfManufacture} W
                                <button className="small-btn" onClick={() => deleteDevice(d.id)}>delete</button>
                            </li>
                        ))}
                    </ul>
                    <input className="input" placeholder="Device name" value={newDevice.name}
                           onChange={e => setNewDevice({...newDevice, name:e.target.value})}/>
                    <input className="input" placeholder="Maximum Consumption" value={newDevice.maximumConsumption}
                           onChange={e => setNewDevice({...newDevice, maximumConsumption:e.target.value})}/>
                    <input className="input" placeholder="Year of Manufacture" value={newDevice.yearOfManufacture}
                           onChange={e => setNewDevice({...newDevice, yearOfManufacture:e.target.value})}/>

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
