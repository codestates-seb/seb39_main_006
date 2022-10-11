import styled from "styled-components";

const H1 = styled.h1`
  margin-left: 1rem;
  font-size: 40px;
  display: block;
  align-items: center;
  font-family: "Montserrat", sans-serif;
  text-transform: uppercase;
  text-shadow: 1px 1px 0px #dabbc9, 2px 2px 0px #dabbc9, 3px 3px 0px #dabbc9,
    4px 4px 0px #dabbc9;
  .delete {
    display: grid;
    grid: auto / repeat(auto-fit, minmax(200px, auto));
    place-items: center;
    font-size: 1.25rem;
    background-color: #dabbc9;
    width: fit-content;
    border: 1px solid #dabbc9;
    padding: 0.5rem 1rem;
    margin: 0.5rem;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
    color: #425049;
    &:hover {
      background-color: #efd5c8;
      border-color: #efd5c8;
    }
    &:disabled {
      border: 1px solid #999999;
      background-color: #cccccc;
      color: #666666;
      pointer-events: none;
    }
  }
`;

export default H1;
