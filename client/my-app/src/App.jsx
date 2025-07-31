import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
const Home = () => <h2>This is Home Page for this project</h2>;
const NotFound=() => <h2>404 Page Not Found</h2>;

function App() {
  return (
    <>

        <switch>
            <Route path="/" exact component={Home} />
            <Route path={"/login"} element={<Login />}></Route>
            <Route path="*" element={<NotFound />} />
        </switch>
    </>

  )
}

export default App
