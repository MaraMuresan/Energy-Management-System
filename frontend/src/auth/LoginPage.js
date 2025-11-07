import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/client";
import { jwtDecode } from "jwt-decode";
import "../index.css";

export default function LoginPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({ username: "", password: "" });
    const [error, setError] = useState("");

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post("/authentication/login", form);
            const token = res.data.token || res.data;
            localStorage.setItem("token", token);
            const decoded = jwtDecode(token);
            const role = Array.isArray(decoded.role) ? decoded.role[0] : decoded.role;
            navigate(role === "ADMIN" ? "/admin" : "/user");
        } catch {
            setError("Invalid username or password.");
        }
    };

    return (
        <div className="page-center">
            <div className="card pink-card">
                <h2 className="title">Welcome back</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        className="input"
                        name="username"
                        placeholder="Username"
                        value={form.username}
                        onChange={handleChange}
                    />
                    <input
                        className="input"
                        type="password"
                        name="password"
                        placeholder="Password"
                        value={form.password}
                        onChange={handleChange}
                    />
                    {error && <p className="error">{error}</p>}
                    <button className="button" type="submit">Login</button>
                </form>
                <p className="text">
                    Not a member?{" "}
                    <Link to="/register" className="link">Register now</Link>
                </p>
            </div>
        </div>
    );
}
