
export interface Item {
  id: string;
  x: number;
  y: number;
  w: number;
  h: number;
  angle: number;
  src: string;
  type: string;
}

export interface Configuration {
  backgroundColor: string;
  backgroundImg: string;
  backgroundPattern: string;
  items: Array<Item>;
}


export interface Image {
  id: string;
  content: string;
}
