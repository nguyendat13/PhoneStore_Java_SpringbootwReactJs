// import React, { useEffect, useState } from 'react';
// import { BlobProvider } from '@react-pdf/renderer';
// import { HiOutlinePrinter } from 'react-icons/hi';
// import MyDocument from './MyDocument';

// const PDFButton = () => {
//     const [data, setData] = useState(null);
//     const [loading, setLoading] = useState(true);
//     const [error, setError] = useState(null);

//     useEffect(() => {
//         const fetchData = async () => {
//             const token = localStorage.getItem('jwt-token');
//             const email = localStorage.getItem('globalEmailCart');

//             if (!token || !email) {
//                 setError(new Error('Missingtoken, or email'));
//                 setLoading(false);
//                 return;
//             }

//             try {
//                 const response = await fetch(
//                     `http://localhost:8080/api/public/users/${email}/carts/${cartId}`,
//                     {
//                         headers: {
//                             'Authorization': `Bearer ${token}`,
//                             'Content-Type': 'application/json',
//                         },
//                     }
//                 );

//                 if (!response.ok) {
//                     throw new Error('Network response was not ok');
//                 }
//                 const result = await response.json();
//                 setData(result);
//             } catch (error) {
//                 setError(error);
//             } finally {
//                 setLoading(false);
//             }
//         };

//         fetchData();
//     }, []);

//     if (loading) return <div>Loading...</div>;
//     if (error) return <div>Error: {error.message}</div>;

//     return (
//         <BlobProvider document={<MyDocument data={data} />}>
//             {({ url }) => (
//                 <button
//                     style={{
//                         padding: '10px 20px',
//                         backgroundColor: '#4CAF50',
//                         color: 'white',
//                         border: 'none',
//                         borderRadius: '5px',
//                         cursor: 'pointer',
//                         display: 'flex',
//                         alignItems: 'center',
//                     }}
//                     onClick={() => window.open(url, '_blank')}
//                 >
//                     <HiOutlinePrinter size={17} style={{ marginRight: '8px' }} />
//                     Print
//                 </button>
//             )}
//         </BlobProvider>
//     );
// };

// export default PDFButton;
