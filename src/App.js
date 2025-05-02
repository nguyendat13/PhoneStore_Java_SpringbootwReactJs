
import Footer from './layouts/Footer';
import Header from './layouts/Header';
import Main from './layouts/Main';
import { BrowserRouter as Router } from 'react-router-dom';

function App() {
  return (
    <Router>
    <Header/>
    <Main/>
    <Footer/>
    </Router>
  );
}

export default App;
