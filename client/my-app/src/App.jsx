import {useEffect, useState} from 'react'
import {Routes, useNavigate,Navigate} from 'react-router-dom';
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import {ToastContainer} from 'react-toastify'
import './App.css'
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Login from "./view/Login/Login.tsx";
import Register from "./view/Register/Register.tsx"
import Home from "./view/Home/Home.js";
import Users from "./view/Users/Users.js";
import UserProfile from "./view/UpdateUser/UserProfile.tsx";
import CreateActivity from "./view/ActivityCreation/createActivity.js";
import StarRatingForm from "./view/Comment/Comment.js";
import ActivityListPage from "./view/Activities/Activities.js";
import ActivityDetailPage from "./view/Activity/Activity.js";

function NotFound() {return <h2>404 Page Not Found</h2>;}
function Activities() {return <h2>404 Page Not Found</h2>;}

function App() {

    return (
        <div className="App">
            <ToastContainer />
            <Router>
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/login" element={<Login />} />
                    <Route path='/home' element={<Home />} />
                    <Route path='/userProfile' element={<UserProfile />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/activities" element={<ActivityListPage/>} />
                    <Route path="/activity" element={<ActivityDetailPage/>} />
                    <Route path="/createActivity" element={<CreateActivity />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/users" element={<Users />} />
                    <Route path="/comment" element={<StarRatingForm />} />
                    <Route path="*" element={<NotFound />} />
                </Routes>
            </Router>
        </div>


  )
}

export default App
