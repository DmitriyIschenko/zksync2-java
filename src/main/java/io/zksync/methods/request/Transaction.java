package io.zksync.methods.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.zksync.transaction.type.Transaction712;
import io.zksync.utils.ContractDeployer;
import io.zksync.utils.ZkSyncAddresses;
import lombok.AllArgsConstructor;
import org.web3j.abi.FunctionEncoder;
import org.web3j.protocol.core.methods.response.AccessListObject;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * Transaction request object used the below methods.
 *
 * <ol>
 *   <li>eth_call
 *   <li>eth_estimateGas
 *   <li>zks_estimateFee
 * </ol>
 */
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    private String from;
    private String to;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger value;
    private String data;

    private Long transactionType;
    private AccessListObject accessList;
    private Eip712Meta eip712Meta;

    public Transaction(String from, String to, BigInteger gas, BigInteger gasPrice, BigInteger value, String data, Eip712Meta eip712Meta) {
        this.from = from;
        this.to = to;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.value = value;
        this.data = data;
        this.eip712Meta = eip712Meta;

        this.transactionType = (long) Transaction712.EIP_712_TX_TYPE;
    }

    public static Transaction createEtherTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String to,
            BigInteger value) {

        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, null, null);
        return new Transaction(from, to, ergsPrice, ergsLimit, value, "0x", meta);
    }

    public static Transaction create2ContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            BigInteger value,
            String bytecode
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        String calldata = FunctionEncoder.encode(ContractDeployer.encodeCreate2(bytecodeBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, value, calldata, meta);
    }

    public static Transaction create2ContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        String calldata = FunctionEncoder.encode(ContractDeployer.encodeCreate2(bytecodeBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldata, meta);
    }

    public static Transaction create2ContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            String calldata
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate2(bytecodeBytes, calldataBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }

    public static Transaction create2ContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            String calldata,
            byte[] salt
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate2(bytecodeBytes, calldataBytes, salt));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }

    public static Transaction create2ContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            List<String> deps,
            String calldata,
            byte[] salt
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        byte[][] factoryDeps = new byte[deps.size() + 1][];
        for (int i = 0; i < deps.size(); i ++) {
            factoryDeps[i] = Numeric.hexStringToByteArray(deps.get(i));
        }
        factoryDeps[deps.size()] = bytecodeBytes;
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate2(bytecodeBytes, calldataBytes, salt));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, factoryDeps, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }

    public static Transaction create2ContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            String calldata,
            String customSignature
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        byte[] customSignatureBytes = Numeric.hexStringToByteArray(customSignature);
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate2(bytecodeBytes, calldataBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), customSignatureBytes, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }

    public static Transaction create2ContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            String calldata,
            String customSignature,
            PaymasterParams paymasterParams
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        byte[] customSignatureBytes = Numeric.hexStringToByteArray(customSignature);
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate2(bytecodeBytes, calldataBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), customSignatureBytes, new byte[][] {bytecodeBytes}, paymasterParams);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }

    public static Transaction create2AccountTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        String calldata = FunctionEncoder.encode(ContractDeployer.encodeCreate2Account(bytecodeBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldata, meta);
    }

    public static Transaction create2AccountTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            String calldata
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate2Account(bytecodeBytes, calldataBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }
    public static Transaction create2AccountTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            String calldata,
            byte[] salt
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate2Account(bytecodeBytes, calldataBytes, salt));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }


    public static Transaction createContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            BigInteger value,
            String bytecode
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        String calldata = FunctionEncoder.encode(ContractDeployer.encodeCreate(bytecodeBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, value, calldata, meta);
    }

    public static Transaction createContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        String calldata = FunctionEncoder.encode(ContractDeployer.encodeCreate(bytecodeBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldata, meta);
    }

    public static Transaction createContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            String calldata
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate(bytecodeBytes, calldataBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, new byte[][] {bytecodeBytes}, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }

    public static Transaction createContractTransaction(
            String from,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String bytecode,
            List<String> deps,
            String calldata
    ) {
        byte[] bytecodeBytes = Numeric.hexStringToByteArray(bytecode);
        byte[] calldataBytes = Numeric.hexStringToByteArray(calldata);
        byte[][] factoryDeps = new byte[deps.size() + 1][];
        for (int i = 0; i < deps.size(); i ++) {
            factoryDeps[i] = Numeric.hexStringToByteArray(deps.get(i));
        }
        factoryDeps[deps.size()] = bytecodeBytes;
        String calldataCreate = FunctionEncoder.encode(ContractDeployer.encodeCreate(bytecodeBytes, calldataBytes));
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, factoryDeps, null);
        return new Transaction(from, ZkSyncAddresses.CONTRACT_DEPLOYER_ADDRESS, ergsPrice, ergsLimit, null, calldataCreate, meta);
    }

    public static Transaction createFunctionCallTransaction(
            String from,
            String to,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            BigInteger value,
            String data
    ) {
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, null, null);
        return new Transaction(from, to, ergsPrice, ergsLimit, value, data, meta);
    }

    public static Transaction createFunctionCallTransaction(
            String from,
            String to,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            String data
    ) {

        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), null, null, null);
        return new Transaction(from, to, ergsPrice, ergsLimit, null, data, meta);
    }

    public static Transaction createFunctionCallTransaction(
            String from,
            String to,
            BigInteger ergsPrice,
            BigInteger ergsLimit,
            BigInteger value,
            String data,
            String customSignature,
            PaymasterParams paymasterParams
    ) {
        Eip712Meta meta = new Eip712Meta(BigInteger.valueOf(160000L), Numeric.hexStringToByteArray(customSignature), null, paymasterParams);
        return new Transaction(from, to, ergsPrice, ergsLimit, value, data, meta);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGas() {
        return convert(gas);
    }

    public String getGasPrice() {
        return convert(gasPrice);
    }

    public String getValue() {
        return convert(value);
    }

    @JsonIgnore
    public BigInteger getGasNumber() {
        return gas;
    }

    @JsonIgnore
    public BigInteger getGasPriceNumber() {
        return gasPrice;
    }

    @JsonIgnore
    public BigInteger getValueNumber() {
        return value;
    }

    public String getData() {
        return data;
    }

    public String getTransactionType() {
        return convert(BigInteger.valueOf(transactionType));
    }

    public AccessListObject getAccessList() {
        return accessList;
    }

    public Eip712Meta getEip712Meta() {
        return eip712Meta;
    }

    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null; // we don't want the field to be encoded if not present
        }
    }

}
