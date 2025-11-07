import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/client";
import "../index.css";

export default function RegisterPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({ username: "", password: "", role: "USER" });
    const [error, setError] = useState("");

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post("/auth/register", form);
            navigate("/login");
        } catch {
            setError("Registration failed. Try again.");
        }
    };

    return (
        <div className="page-center">
            <div className="card pink-card">
                <h2 className="title">Create account ✨</h2>
                <form onSubmit={handleSubmit}>
                    <input className="input" name="username" placeholder="Username" value={form.username} onChange={handleChange} />
                    <input className="input" type="password" name="password" placeholder="Password" value={form.password} onChange={handleChange} />
                    <select className="input" name="role" value={form.role} onChange={handleChange}>
                        <option value="USER">User</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                    {error && <p className="error">{error}</p>}
                    <button className="button" type="submit">Sign Up</button>
                </form>
                <p className="text">
                    Already have an account?{" "}
                    <Link to="/login" className="link">Login</Link>
                </p>
            </div>
        </div>
    );
}
