import { css } from "styled-components";

export const theme = {
  mainBlack: "#000000",
  mainWhite: "#FFFFFF",
  mainPink: "#FF385C",
  mainGrey: "#F7F7F7",
  darkGrey: "#797979",

  fontLogo: "'Song Myung', serif",

  fontLarge: "48px",
  fontMedium: "28px",
  fontSemiMedium: "20px",
  fontRegular: "18px",
  fontSmall: "16px",
  fontMicro: "14px",

  weightBold: 700,
  weightSemiBold: 600,
  weightRegular: 400,

  absoluteCenter: css`
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  `,
};

export const mixins = {
  // flex
  flexBox: (direction = "row", align = "center", justify = "center") => `
    display: flex;
    flex-direction: ${direction};
    align-items: ${align};
    justify-content: ${justify};
  `,
};
