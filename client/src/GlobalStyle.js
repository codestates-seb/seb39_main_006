import { createGlobalStyle } from "styled-components";
import reset from "styled-reset";

const GlobalStyle = createGlobalStyle`
  ${reset}
  * {
    box-sizing : border-box;
  }
  body {
    font-family: 'Chakra Petch', sans-serif;
    font-family: 'Gaegu', cursive;
    font-family: 'Noto Serif KR', serif;
  }
`;

export default GlobalStyle;
