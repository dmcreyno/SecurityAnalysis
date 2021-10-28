# SecurityAnalysis
Core functionality for analyzing trade data in a platform independent manner. You must provide two classes (See [Fidelity Security Analysis](https://github.com/dmcreyno/FidelitySecurityAnalysis)) for an example using the file format from Fidelity Investments. The two class files you provide will be responsible for reading the data you download from your provider. I used Fidelity because that is where my accounts are.

The entry point is defined in this project: `static com.cobbinterwebs.trades.Main.main(String[] args): void`

To run the application you will need to define the "Home Directory." This directory should contain sub-directories, one for each company trading symbol under analysis.
Use the VM argument -Dcom.cobbinterwebs.trades.home to pass the value to the application.

## Dependencies
- [Security Analysis](https://github.com/dmcreyno/SecurityAnalysis)
- [JWave - Forked](https://github.com/dmcreyno/JWave)
- [JWave - Original Project](https://github.com/graetz23/JWave)

### JWave

## Example
-Dcom.cobbinterwebs.trades.home=c:\trading-data

The trading-data directory could have sub-directories: MSFT, GWRE.

To provide finer grained control over execution, the application will expect you to pass in the trading symbols to be analyzed for the current run.

Also, if you specify a trading symbol and that symbol does not exist, the application will fill out the directory structures for you. You can then copy the trading data you have into the <symbol>/input directory.

## Command Line Example
java -Dcom.cobbinterwebs.trades.home=c:\trading-data GWRE MSFT
