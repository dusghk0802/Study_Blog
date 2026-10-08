import Button from './components/Button';
import Header from './components/Header';
import Footer from './components/Footer'; 

function App() {
  return (
    <>
      <Header />
      <main>
        <Button onClick={() => alert('Button clicked!')}>
          Click me
        </Button>
      </main>
      <Footer />
    </>
  )
}

export default App
