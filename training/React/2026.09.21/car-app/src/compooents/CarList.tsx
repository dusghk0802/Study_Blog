import React, { useEffect, useState } from 'react'; 
import type { Car } from '../types/Car'; 
import './CarList.css'; // 스타일 적용 
 
const CarList: React.FC = () => { 
  const [cars, setCars] = useState<Car[]>([]); 
  const [loading, setLoading] = useState<boolean>(true); 
  const [error, setError] = useState<string | null>(null); 
 
  useEffect(() => { 
    fetch('http://localhost:8080/cars') 
      .then((res) => { 
        if (!res.ok) { 
          throw new Error('서버 응답 오류'); 
        } 
        return res.json(); 
      }) 
      .then((data: Car[]) => { 
        setCars(data); 
        setLoading(false); 
      }) 
      .catch((err: Error) => { 
        setError(err.message); 
        setLoading(false); 
      }); 
  }, []); 
 
  if (loading) return <p>로딩 중...</p>; 
  if (error) return <p>오류 발생: {error}</p>; 
 
  return ( 
    <div className="carlist-container"> 
      <h2>          자동차 목록</h2> 
      <table className="carlist-table"> 
        <thead> 
          <tr> 
            <th>브랜드</th> 
            <th>모델</th> 
            <th>색상</th> 
            <th>연식</th> 
            <th>가격</th> 
            <th>등록번호</th> 
            <th>소유자</th> 
          </tr> 
        </thead> 
        <tbody> 
          {cars.map((car) => ( 
            <tr key={car.id}> 
              <td>{car.brand}</td> 
              <td>{car.model}</td> 
              <td>{car.color}</td> 
              <td>{car.modelYear}</td> 
              <td>${car.price.toLocaleString()}</td> 
              <td>{car.registrationNumber}</td> 
              <td> 
                {car.owner.firstname} {car.owner.lastname} 
              </td> 
            </tr> 
          ))} 
        </tbody> 
      </table> 
    </div> 
  ); 
}; 
 
export default CarList;