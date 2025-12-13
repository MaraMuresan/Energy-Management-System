import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./auth/LoginPage";
import RegisterPage from "./auth/RegisterPage";
import AdminDashboard from "./admin/AdminDashboard";
import UserDashboard from "./user/UserDashboard";
import DailyConsumptionPage from "./user/DailyConsumptionPage";
import AdminChat from "./admin/AdminChat";

export default function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/admin" element={<AdminDashboard />} />
          <Route path="/user" element={<UserDashboard />} />
            <Route path="/user/energy" element={<DailyConsumptionPage />} />
            <Route path="/admin/chat" element={<AdminChat />} />
        </Routes>
      </BrowserRouter>
  );
}
