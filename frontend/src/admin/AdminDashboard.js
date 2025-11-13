import React, { useEffect, useState } from "react";
import api from "../api/client";
import "../index.css";

export default function AdminDashboard() {
    const [users, setUsers] = useState([]);
    const [devices, setDevices] = useState([]);
    const [newUser, setNewUser] = useState({ username: "", address: "", age: 0, password: "", role: "USER" });
    const [newDevice, setNewDevice] = useState({ name: "", maximumConsumption: 0.0, yearOfManufacture: 2015 });
    const [assignUser, setAssignUser] = useState("");
    const [assignDevices, setAssignDevices] = useState([]);

    const [editingUser, setEditingUser] = useState(null);
    const [editData, setEditData] = useState({ username: "", address: "", age: 0 });

    const [editingDevice, setEditingDevice] = useState(null);
    const [editDeviceData, setEditDeviceData] = useState({ name: "", maximumConsumption: 0, yearOfManufacture: 2015 });

    const getErrorMessage = (err) => {
        if (err.response) {
            const data = err.response.data;
            if (typeof data === "string") return data;
            if (data.message) return data.message;
            if (data.error) return data.error;
            if (data.detail) return data.detail;
        }
        return err.message || "An unknown error occurred.";
    };

    const loadAll = async () => {
        const u = await api.get("/user");
        const d = await api.get("/device");
        setUsers(u.data);
        setDevices(d.data);
    };

    useEffect(() => { loadAll(); }, []);

    const addUser = async () => {
        const registerPayload = {
            username: newUser.username,
            address: newUser.address,
            age: newUser.age,
            password: newUser.password,
            role: newUser.role
        };

        try {
            await api.post("/authentication/register", registerPayload);
            setNewUser({ username: "", address: "", age: 0, password: "", role: "USER" });
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to add user: ${msg}`);
            console.error("Add user error:", err);
        }
    };

    const updateUser = async (id) => {
        try {
            await api.put(`/user/${id}`, editData);
            setEditingUser(null);
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to update user: ${msg}`);
            console.error("Update user error:", err);
        }

    };

    const deleteUser = async (id) => {
        try {
            await api.delete(`/user/${id}`);
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to delete user: ${msg}`);
            console.error("Delete user error:", err);
        }
    };

    const addDevice = async () => {
        try {
            await api.post("/device", newDevice);
            setNewDevice({ name: "", maximumConsumption: 0.0, yearOfManufacture: 2015 });
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to add device: ${msg}`);
            console.error("Add device error:", err);
        }
    };

    const updateDevice = async (id) => {
        try {
            await api.put(`/device/${id}`, editDeviceData);
            setEditingDevice(null);
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to update device: ${msg}`);
            console.error("Update device error:", err);
        }
    };

    const deleteDevice = async (id) => {
        try {
            await api.delete(`/device/${id}`);
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to delete device: ${msg}`);
            console.error("Delete device error:", err);
        }
    };

    const assign = async () => {
        if (!assignUser || assignDevices.length === 0) return;

        try {
            await Promise.all(
                assignDevices.map(deviceId => {
                    const d = devices.find(x => x.id === deviceId);
                    if (!d) return Promise.resolve();

                    const body = {
                        name: d.name,
                        maximumConsumption: d.maximumConsumption,
                        yearOfManufacture: d.yearOfManufacture,
                        userId: assignUser,
                    };

                    return api.put(`/device/${deviceId}`, body);
                })
            );

            setAssignDevices([]);
            alert("Devices assigned successfully!");
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to assign device: ${msg}`);
            console.error("Assign device error:", err);
        }
    };

    const unassignDevice = async (deviceId) => {
        try {
            const d = devices.find(x => x.id === deviceId);
            if (!d) return;

            const body = {
                name: d.name,
                maximumConsumption: d.maximumConsumption,
                yearOfManufacture: d.yearOfManufacture,
                userId: null,
            };

            await api.put(`/device/${deviceId}`, body);
            alert(`Device unassigned successfully!`);
            loadAll();
        } catch (err) {
            const msg = getErrorMessage(err);
            alert(`Failed to unassign device: ${msg}`);
            console.error("Unassign device error:", err);
        }
    }

    return (
        <div className="page-center">
            <div className="card large-card">
                <h2 className="title">Admin Dashboard</h2>

                <section>
                    <h3>Users</h3>
                    <ul className="list">
                        {users.map(u => (
                            <li key={u.id}>
                                {editingUser === u.id ? (
                                    <div className="edit-user">
                                        <input className="input" value={editData.username}
                                               onChange={e => setEditData({...editData, username: e.target.value})} />
                                        <input className="input" value={editData.address}
                                               onChange={e => setEditData({...editData, address: e.target.value})} />
                                        <input className="input" value={editData.age}
                                               onChange={e => setEditData({...editData, age: e.target.value})} />
                                        <button className="small-btn save" onClick={() => updateUser(u.id)}>Save</button>
                                        <button className="small-btn cancel" onClick={() => setEditingUser(null)}>Cancel</button>
                                    </div>
                                ) : (
                                    <>
                                        {u.username}
                                        <button className="small-btn edit" onClick={() => {
                                            setEditingUser(u.id);
                                            setEditData({
                                                username: u.username,
                                                address: u.address,
                                                age: u.age
                                            });
                                        }}>Edit</button>
                                        <button className="small-btn delete" onClick={() => deleteUser(u.id)}>Delete</button>
                                    </>
                                )}
                            </li>
                        ))}
                    </ul>

                    <div className="add-form">
                        <input className="input" placeholder="Username" value={newUser.username}
                               onChange={e => setNewUser({...newUser, username:e.target.value})}/>
                        <input className="input" placeholder="Address" value={newUser.address}
                               onChange={e => setNewUser({...newUser, address:e.target.value})} />
                        <input className="input" placeholder="Age" value={newUser.age}
                               onChange={e => setNewUser({...newUser, age:e.target.value})} />
                        <input className="input" type="password" placeholder="Password" value={newUser.password}
                               onChange={e => setNewUser({...newUser, password:e.target.value})} />
                        <select className="input" value={newUser.role}
                                onChange={e => setNewUser({...newUser, role:e.target.value})}>
                            <option value="USER">User</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                        <button className="button" onClick={addUser}>Add User</button>
                    </div>
                </section>

                <hr/>

                <section>
                    <h3>Devices</h3>
                    <ul>
                        {devices.map(d => (
                            <li key={d.id}>
                                {editingDevice === d.id ? (
                                    <div className="edit-device">
                                        <input className="input" value={editDeviceData.name}
                                               onChange={e => setEditDeviceData({...editDeviceData, name: e.target.value})}/>
                                        <input className="input" value={editDeviceData.maximumConsumption}
                                               onChange={e => setEditDeviceData({...editDeviceData, maximumConsumption: e.target.value})}/>
                                        <input className="input" value={editDeviceData.yearOfManufacture}
                                               onChange={e => setEditDeviceData({...editDeviceData, yearOfManufacture: e.target.value})}/>
                                        <button className="small-btn save" onClick={() => updateDevice(d.id)}>Save</button>
                                        <button className="small-btn cancel" onClick={() => setEditingDevice(null)}>Cancel</button>
                                    </div>
                                ) : (
                                    <>
                                        {d.name}, {d.maximumConsumption}W, {d.yearOfManufacture}
                                        {d.userReplicaDTO ? (
                                            <>
                                                {` — assigned to ${d.userReplicaDTO.username} `}
                                                <button className="small-btn unassign" onClick={() => unassignDevice(d.id)}>Unassign</button>
                                            </>
                                        ) : (
                                            " — unassigned"
                                        )}
                                        <button className="small-btn edit" onClick={() => {
                                            setEditingDevice(d.id);
                                            setEditDeviceData({
                                                name: d.name,
                                                maximumConsumption: d.maximumConsumption,
                                                yearOfManufacture: d.yearOfManufacture
                                            });
                                        }}>Edit</button>
                                        <button className="small-btn delete" onClick={() => deleteDevice(d.id)}>Delete</button>
                                    </>
                                )}
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
                                {d.name}, {d.maximumConsumption}W, {d.yearOfManufacture}
                            </label>
                        ))}
                    </div>
                    <button className="button" disabled={!assignUser || assignDevices.length === 0} onClick={assign}>Assign</button>
                </section>
            </div>
        </div>
    );
}
