import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import loginPage from "./auth/loginPage";
import registerPage from "./auth/registerPage";
import adminDashboard from "./admin/adminDashboard";
import userDashboard from "./user/userDashboard";

export default function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<loginPage />} />
          <Route path="/register" element={<registerPage />} />
          <Route path="/admin" element={<adminDashboard />} />
          <Route path="/user" element={<userDashboard />} />
        </Routes>
      </BrowserRouter>
  );
}
