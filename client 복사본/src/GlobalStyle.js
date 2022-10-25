import styled, { createGlobalStyle } from "styled-components";

const GlobalStyle = createGlobalStyle`
    * {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
        list-style: none;
        font-family: 'Noto Sans KR', sans-serif;
    }

    //클래스명으로 넣으면 무조건 감춰짐
    .hidden,
    [hidden] {
    display: none !important;
    }
`;

export const Layout = styled.main``;

export const Container = styled.div``;

export default GlobalStyle;
