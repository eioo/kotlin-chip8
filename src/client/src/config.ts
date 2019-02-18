import * as dotenv from 'dotenv';
import * as path from 'path';

dotenv.config({
  path: path.join(__dirname, '../../.env'),
});

export default {
  host: process.env.HOST || 'localhost',
  port: parseInt(process.env.PORT || '', 10) || 8080,
};
