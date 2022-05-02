package io.zksync.transaction;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.utils.Convert.Unit;

import io.zksync.abi.TransactionEncoder;
import io.zksync.crypto.eip712.Eip712Domain;
import io.zksync.crypto.eip712.Eip712Encoder;
import io.zksync.protocol.core.Token;
import io.zksync.protocol.core.ZkSyncNetwork;

public class WithdrawTest extends BaseTransactionTest {

    private static final Token WITHDRAW_TOKEN = BaseTransactionTest.FEE_TOKEN;
    private static final String RECEIVER = "0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC";

    @Test
    public void testSerializeToEIP712() {
        Withdraw withdraw = buildWithdraw();
        List<Pair<String, Type<?>>> types = withdraw.eip712types();
        Iterator<Pair<String, Type<?>>> t = types.iterator();

        {
            Pair<String, Type<?>> t2 = t.next();
            assertEquals("to", t2.getKey());
            assertEquals(new Address(RECEIVER), t2.getValue());
        }
        {
            Pair<String, Type<?>> t2 = t.next();
            assertEquals("token", t2.getKey());
            assertEquals(new Address(WITHDRAW_TOKEN.getAddress()), t2.getValue());
        }
        {
            Pair<String, Type<?>> t2 = t.next();
            assertEquals("amount", t2.getKey());
            assertEquals(new Uint256(BigInteger.valueOf(1000000000000000000L)), t2.getValue());
        }
        super.assertSerializeToEIP712(t);
    }

    @Test
    public void testEncodeToEIP712TypeString() {
        Withdraw withdraw = buildWithdraw();
        String result = Eip712Encoder.encodeType(withdraw.intoEip712Struct());

        assertEquals(
                "Withdraw(address to,address token,uint256 amount,address initiatorAddress,address feeToken,uint256 fee,uint32 nonce,uint64 validFrom,uint64 validUntil)",
                result);
    }

    @Test
    public void testSerializeToEIP712EncodedValue() {
        Withdraw withdraw = buildWithdraw();
        byte[] encoded = Eip712Encoder.encodeValue(withdraw.intoEip712Struct()).getValue();

        assertEquals("0x3a99876e01820f4d66c90ed96be2dc0c1f44b0d7d490e2af4a7ab498c2bcb1ad",
                Numeric.toHexString(encoded));
    }

    @Test
    public void testSerializeToEIP712Message() {
        Withdraw withdraw = buildWithdraw();
        byte[] encoded = Eip712Encoder.typedDataToSignedBytes(Eip712Domain.defaultDomain(ZkSyncNetwork.Localhost),
                withdraw);

        assertEquals("0xb7735ab5994e9baec8aed00895c54574a513ad7d2b96965cee0ad66ff0e405f0",
                Numeric.toHexString(encoded));
    }

    private Withdraw buildWithdraw() {
        return new Withdraw(
                WITHDRAW_TOKEN.getAddress(),
                RECEIVER,
                Convert.toWei("1", Unit.ETHER).toBigInteger(),
                SENDER.getAddress(),
                FEE,
                BigInteger.valueOf(NONCE));
    }
}
