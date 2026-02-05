package com.example.testcryptoapp.repository

import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.Chains.EVM.convertEthToWei
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Models.BaseWallet
import com.dynamic.sdk.android.Models.Network
import com.example.testcryptoapp.model.TxStatus
import com.example.testcryptoapp.model.WalletModel
import com.example.testcryptoapp.repositories.WalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.JsonPrimitive
import java.math.BigInteger

class WalletRepositoryImpl(private val dynamicSDK: DynamicSDK) : WalletRepository {
    override fun observeWallet(): Flow<WalletModel> = flow {
        dynamicSDK.wallets.userWallets
            .firstOrNull { it.chain == "EVM" }
            ?.let { wallet ->
                emit(loadWallet(wallet))
            }
        dynamicSDK.wallets.userWalletsChanges.collect { wallets ->
            val wallet = wallets.firstOrNull { it.chain == "EVM" }
            if (wallet != null) {
                emit(loadWallet(wallet))
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun loadWallet(wallet: BaseWallet): WalletModel {
        val sepoliaNetwork = Network(JsonPrimitive(11155111))
        dynamicSDK.wallets.switchNetwork(wallet, sepoliaNetwork)

        val network = dynamicSDK.networks.evm.find { it.name == "Sepolia" }
        val balance = dynamicSDK.wallets.getBalance(wallet)

        return WalletModel(
            address = wallet.address,
            balance = balance,
            chain = network?.chainId.toString(),
            walletProvider = wallet.chain,
            walletName = network?.name
        )
    }

    override fun sendTransaction(
        recipientAddress: String,
        amount: String
    ): Flow<TxStatus> = flow {
        emit(TxStatus.Loading)

        val wallet = dynamicSDK.wallets.userWallets
            .firstOrNull { it.chain == "EVM" }
            ?: throw IllegalStateException("EVM wallet not found")

        val sepoliaNetwork = Network(JsonPrimitive(11155111))
        dynamicSDK.wallets.switchNetwork(wallet, sepoliaNetwork)

        val weiAmount: BigInteger = convertEthToWei(amount)
        val client = dynamicSDK.evm.createPublicClient(chainId = 11155111)
        val gasPrice = client.getGasPrice()

        val tx = EthereumTransaction(
            from = wallet.address,
            to = recipientAddress,
            value = weiAmount,
            gas = BigInteger.valueOf(21000),
            maxFeePerGas = gasPrice * BigInteger.valueOf(2),
            maxPriorityFeePerGas = gasPrice
        )

        val txHash = dynamicSDK.evm.sendTransaction(tx, wallet)

        emit(TxStatus.Success(txHash))
    }.flowOn(Dispatchers.IO)
}
