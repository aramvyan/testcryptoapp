package com.example.testcryptoapp.repository

import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.Chains.EVM.convertEthToWei
import com.dynamic.sdk.android.DynamicSDK
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
    override fun getWallet(): Flow<WalletModel> = flow {
        val wallet = dynamicSDK.wallets.userWallets
            .firstOrNull { it.chain == "EVM" }
            ?: throw IllegalStateException("EVM wallet not found")
        val sepoliaNetwork = Network(JsonPrimitive(11155111))
        dynamicSDK.wallets.switchNetwork(wallet, sepoliaNetwork)

        val network = dynamicSDK.networks.evm
        val genericNetwork = network.find { it.name == "Sepolia" }
        val chainId = genericNetwork?.chainId.toString()
        val networkName = genericNetwork?.name
        val balance = dynamicSDK.wallets.getBalance(wallet)

        emit(
            WalletModel(
                address = wallet.address,
                balance = balance,
                chain = chainId,
                walletProvider = wallet.chain,
                walletName = networkName
            )
        )
    }.flowOn(Dispatchers.IO)

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

        val tx = EthereumTransaction(
            from = wallet.address,
            to = recipientAddress,
            value = weiAmount
        )

        val txHash = dynamicSDK.evm.sendTransaction(tx, wallet)

        emit(TxStatus.Success(txHash))
    }.flowOn(Dispatchers.IO)
}
