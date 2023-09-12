package com.microsoft.band.service.crypto;
/* loaded from: classes.dex */
public interface CryptoProvider {
    String decrypt(String str, int i) throws CryptoException;

    String encrypt(String str, int i) throws CryptoException;
}
