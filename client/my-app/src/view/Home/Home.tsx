import UserPanel from "../Tool/UserPanel";
import './Home.css'
import {useEffect, useRef, useState} from "react";

const Home=()=>{
    const [currentIndex, setCurrentIndex] = useState(0);
    const images = [
        "../../../public/title01.JPG",
        "../../../public/title02.jpg",
        "../../../public/title03.png",
    ];


    const intervalRef = useRef(0);

    useEffect(() => {
        intervalRef.current = setInterval(() => {
            setCurrentIndex(prevIndex => {
                let newIndex = prevIndex + 1;
                if (newIndex >= images.length) {
                    newIndex = 0;
                }
                return newIndex;
            });
        }, 3000);

        return () => clearInterval(intervalRef.current);
    }, []); // 保持空依赖数组

    // 控制图片切换的函数
    const moveSlide = (step: any) => {
        let newIndex = currentIndex + step;
        if (newIndex >= images.length) {
            newIndex = 0;
        } else if (newIndex < 0) {
            newIndex = images.length - 1;
        }
        setCurrentIndex(newIndex);
    }

    const resetInterval = () => {
        clearInterval(intervalRef.current);
        intervalRef.current = setInterval(() => {
            setCurrentIndex(prevIndex => {
                let newIndex = prevIndex + 1;
                if (newIndex >= images.length) {
                    newIndex = 0;
                }
                return newIndex;
            });
        }, 3000);
    }

    return (
        <div>
            <UserPanel></UserPanel>
            <h2 style={{textAlign:"center"}}>这是{''}<span ><a href={"https://www.nju.edu.cn/"}>南京大学</a></span>暑校web开发大作业</h2>
            <div className="banner">
                <div className="carousel">
                    <div className="carousel-images">
                        <img
                            src={images[currentIndex]}
                            alt={`Image ${currentIndex + 1}`}
                            className="carousel-image"
                        />
                    </div>
                    <button className="prev" onClick={() => {moveSlide(-1);resetInterval()}}>&#10094;</button>
                    <button className="next" onClick={() => {moveSlide(1);resetInterval()}}>&#10095;</button>
                </div>
            </div>
        </div>
    )
}

export default Home;