import { useState, useEffect } from 'react';

function WeatherFetcher() {
  const [weather, setWeather] = useState({temp: '', desc: '', icon: ''});
 
  useEffect(() => { 
    fetch('https://api.openweathermap.org/data/2.5/weather?q=Seoul&APPID=ded364c965f01d38cd9fcff4cf83f19a&units=metric')
    .then(response => response.json())
    .then(result => {
      setWeather({
        temp: result.main.temp, 
        desc: result.weather[0].main, 
        icon: result.weather[0].icon
      });
    })
    .catch(err => console.error(err))
  }, [])

  if (weather.icon) {
    return (
      <>
        <p>Temperature: {weather.temp} °C</p>
        <p>Description: {weather.desc}</p>
        <img src={`https://openweathermap.org/img/wn/${weather.icon}@2x.png`} alt="Weather icon" />
      </>
    );
  }
  else {
    return <>Loading...</>
  }
}

export default WeatherFetcher;