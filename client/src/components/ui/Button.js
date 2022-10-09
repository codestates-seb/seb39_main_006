import styled from "styled-components";

export const SubmitBtn = styled.button`
  /* display: grid;
  grid: auto / repeat(auto-fit, minmax(200px, auto));
  place-items: center; */
  display: inline-block;
  margin: 0.5rem auto;
  font-size: 1.25rem;
  background-color: #8fc9e0;
  width: 87%;
  /* border: 1px solid #ceebdd; */
  border: none;
  padding: 0.7rem 1.2rem;
  margin: 0.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.4);
  border-radius: 5px;
  font-weight: 600;
  color: white;
  &:hover {
    background-color: #5e98ae;
    /* border-color: #efd5c8; */
  }
  &:disabled {
    border: 1px solid #999999;
    background-color: #cccccc;
    color: #666666;
    pointer-events: none;
  }
`;

const StyledBtn = styled.button`
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
`;

export default StyledBtn;
