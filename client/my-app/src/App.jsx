import {useEffect, useState} from 'react'
import {Routes, useNavigate,Navigate} from 'react-router-dom';
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import {ToastContainer} from 'react-toastify'
import './App.css'
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Login from "./view/Login/Login.tsx";
import "./view/Login/Login.css"

function NotFound() {return <h2>404 Page Not Found</h2>;}
function Register() {return <h2>404 Page Not Found</h2>;}
function Activities() {return <h2>404 Page Not Found</h2>;}

function App() {

    return (
        <div className="App">
            <ToastContainer />
            <Router>
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/activities" element={<Activities />} />
                    <Route path="*" element={<NotFound />} />
                </Routes>
            </Router>
        </div>


  )
}

export default App
