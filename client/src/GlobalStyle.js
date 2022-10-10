import reset from "styled-reset";
import { createGlobalStyle } from "styled-components";

const GlobalStyle = createGlobalStyle`

  ${reset}
  *, *::before, *::after {
  
   font-family: 'IBM Plex Sans KR', sans-serif;
  }

 
`;
export default GlobalStyle;
