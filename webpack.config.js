const path = require('path');
const root = path.resolve(__dirname, 'src/main/webapp');
const src = path.resolve(root, 'javascript');
const dest = path.resolve(__dirname, 'src/main/webapp/dist');
const webpack = require('webpack');

const prod = process.argv.indexOf('-p') !== -1;

const config = {
    entry: src + '/index.jsx',
    devtool: 'sourcemaps',
    output: {
        filename: 'bundle.js',
        path: dest
    },
    resolve: {
        extensions: ['.js', '.jsx']
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['env','stage-2']
                    }
                }
            }
        ]
    }
};

config.plugins = config.plugins||[];
if (prod) {
    config.plugins.push(new webpack.DefinePlugin({
        'process.env': {
            'NODE_ENV': `"production"`
        }
    }));
} else {
    config.plugins.push(new webpack.DefinePlugin({
        'process.env': {
            'NODE_ENV': `""`
        }
    }));
}

module.exports = config;