package com.yoghurt.crypto.transactions.client.util.transaction;

import java.util.ArrayList;

import com.yoghurt.crypto.transactions.shared.domain.Transaction;
import com.yoghurt.crypto.transactions.shared.domain.TransactionInput;
import com.yoghurt.crypto.transactions.shared.domain.TransactionOutPoint;
import com.yoghurt.crypto.transactions.shared.domain.TransactionOutput;
import com.yoghurt.crypto.transactions.shared.domain.VariableLengthInteger;
import com.yoghurt.crypto.transactions.shared.util.ArrayUtil;
import com.yoghurt.crypto.transactions.shared.util.NumberParseUtil;

public final class TransactionParseUtil extends TransactionUtil {
  private TransactionParseUtil() {}

  public static Transaction parseTransactionBytes(final byte[] bytes) {
    return parseTransactionBytes(bytes, new Transaction());
  }

  public static Transaction parseTransactionBytes(final byte[] bytes, final Transaction transaction) {
    return parseTransactionBytes(bytes, transaction, 0);
  }

  public static Transaction parseTransactionBytes(final byte[] bytes, final Transaction transaction, final int initialPointer) {
    int pointer = initialPointer;

    // Parse the version bytes
    pointer = parseVersion(transaction, pointer, bytes);

    // Parse the transaction input size
    pointer = parseTransactionInputSize(transaction, pointer, bytes);

    System.out.println("Input size: " + transaction.getInputSize().getValue());

    // Parse the transaction inputs
    pointer = parseTransactionInputs(transaction, pointer, bytes);

    // for: 01000000
    // 01
    // 0000000000000000000000000000000000000000000000000000000000000000
    // ffffffff
    // 04 02940c00
    // ffffffff
    // 0000000000000000 // Explicit fee?
    // 010000000000000000000000000000000000000000000000000000000000000000000000
    // 23 2102c73d8c656f003ac4826ab93a8a874183ea5b7bb8e2108ce52697c1e76717ef46ac 00000000
    //
    // version
    // input size (1)
    // 32 byte prev tx
    // outpoint index (max)
    // coinbase script (04 len)
    // sequence (max)
    // Explicit fee (8 byte)
    // output num
    // serValue (0, 33 byte)
    // output script size (23 hex)
    // script -> pushdata (21 hex, 33 dec, pubkey) + data
    // locktime

    // #tx:id:fc8127dccd7760ea4e4c505e60fb217b4758f9bf9f3d988785e28b8f127ef51f
    // for: 01000000 01 aa870c02ccb208914aba524f7543d3ea0afc9bce4738e0fb0c34f7f1311de5ce 00000000 64
    // 412f6ad09b15a39cb3e28a34fe33688cbae9b1d2dc7ab39d746dab234ebc6d5f959b12f0ff1c3fd3391ad8bdb27edb8696141e67cd400e3a3b5da4654d1b73deb501210332af36fa5d949df148af14a376a6c20625b26c57a6e25e56dd05327b3dc7caae
    // ffffffff
    // 6715000000000000
    // 02
    // 028be624fb11aa782e2bf450152800534509162ada081721afa373410b359e2b26fd040a401f4d08e0cf4e167de39d2d8f50b9fc83842e8b2dac6dd0efbbf07eba93e312f05c14f043484666d52deae2b130e3f1a2d33c7b6cc28ad76681f4b462bdfb9864812cae829bdf501ab5ce91ea1a871c9e5fd6897197770dc1a0e81b4172ac4284feae947028789dbd2e84883afb8d8f6169ac6164a1b9b178289409acf0ee0e2fa618798ced77bae28782a58553a67964997240e27ed9e2b8cbad82c19c8435265524de9f402d15b2dbb71ff15b56618d136309ffbc96ab45335c04663d84c582b04b6f210948008174ffe0d3278e5ce4748998db6dd258680229dc3347512433f2dad45eab0cfb74a2a910d2d2bcda98eb5c4af21f300fb533c45a143cb74caecbe8ec212315f5084337bf3a83e8124b0ccd735c7eda58ba9a173b64aebedd94f64b270965777bbfe421dac0f748ffefaf7ce35ff582eb568a3debb82f39b6fc9150f70051db06fc155b8485439fa5ef7783c9a9645934306d565dc8e4ed9eb439f249fb81bb60d7a7ba0b6896e4731a83a5e350df5bb51fa86029dccdcb4f7e54dbac70858953256f6f7d5df892187c9bafd92a4f853c9eeec02e0a93ffc34a0d949ba4cb70fe3e4b4816d9efaedf9053476c199da81fc5d19137ced9edc2be21c4ae90e6140c16f23b78ddae4c02fbe53a2a09f446d8bad267c18edb381e06fbfb94237d3b54fbed16d2404f0f2bb5198bc201b97bf318b2d91e7a841346e45293a784cc0bbc55283110f31837586c7665ace8be0fe4fce20a5f3d4d2791d5ce1ea931be0743f7a890acea9b7d546eec2cd66f98854a0bd8eafec85b2bbe65ad9d7dbf693ae326269d32bdfec9476fdf9024cb27476f72d73daf5e0114e40341885d9b870962b27e4ab95c80e0b69633308ef42fe9ccedf780b79de8dd5199eee83b7919a3809b896221b966a086b4dfbbaa80b0d6f3fadb5b18b4ecaff466812fced8344e2f8c877dbd37108e56ffc792e7464eb13fa57985770ce3f71ec6813455557d0a9b10f0416597e73e2fc9f486d09655cabc953acc41b9d0c65a80d038339d4443c6aa8c2ce7037ff47fe4c0f7d3a8b0a524e007b1403f7ef474a93f447a1b6d8cfb63244ace4e8cde6f180960aa3a2b95e1a6c097caf3edab21635a2cb4f150d2caa4072815a2998e2fb94b0c111e06a45826753134ead534bc6fb7e1636d9bac8bd10e42dac8b4a9c0b259c7f6ff824df67450a14124e9139988425520e2f96b5cc560c198207ae71fa3ee6cf5218ec0bd1e59a88905a29cff90fff7710be0918bdd2ce08f9c81504b5aa14beb94bcf6102ee6b324a963f389f25e8fd8e79b1a52232055b192ddf2510eadc1209691cbb44e04125cc073db11b8257dce5a3b478c108d0feacf8b316aa6e9c0bea6db52e82f6c4df0a6ab53466d436e97e0e3f7be7b9aaa10d657e0c83afde810579c24359daa9692024db63e5a710f9a6d1f93a01c4f3c3c1762564225b7a027b3cf4721eae75833bef647c5b00ae98d72a6e5c6c182281535246ad49874f5117d958486d0bbc22d72cd1c2918b5da665f367dec09c6081b07a32723bf115cd8876ddb1b303e76f00d8425b73cbdd399fa01845b5f4120b02e2ec9ec47544308abee185cbd234f66478b778199749336fe74bf7075abd92d9892f53d9e3b123476ba838af347a9ab3535432025b7f37c192711fdd45efee4a0cacd8cdd44524c35cb457a931d966358b63d8481938521b7504f440ac7ff9bb98ebae75592a36bd9571ef5af717c24465e3ee319c87c8f4334ca5885bb099ec6f37bba3b12c883a46f19a280e34823856ab875fb8549422c2915345f19fc02fde5289a961b8d3e589f5c81ca4ab027ad03ce257ae32280aa1f2c6d21f20fac027cd598d8b97a87095b73fa639454bfb09f1fc1b429daf06a612c53ab4321082490991da30bac073eabcf433d81ca398585b14185e9b7fd3c39c62e2fc997fc18d22b003a84585aeb30e7382edca4a3005203d40388facebca019ef51c359e4083606c173c6005187ef8b3af8bed03a64259324346495a24a29a03564fb9f6ca7afc0edf77e8793f40077ae9de38cfb68ef9595a49fe82a9f69b893099a0013194f3588cb06c4c81e6f7431d604cc2f3bc340e5d0a678f6e0f88aefe7e1d02c74a35af32ab118eec451c3cedf475b32a886232f31f0916a3eea2cec9509886e9ec6ace4f3ae68a0bc06d6b7ef478bd2b6e00c6c14e8282cdc13bb16f92ca05c77eb981fdf76d9c1aef17cb04183c48ef369a30b5616731a77a8ebed03605759059a7406105f3feed4875f08b603aa6e7a9eec4b7183eb264b7f79f2f53b6f476a8ceb6dabc31a5a5c74222b73ac95c1596e18e879733d5ff2576b9d04f172682d146e9daa41d394d3216a13d0fb3daba12ca92fd3bebbc8f73f2f79bb71e4c1ed19dde09dd7a320d25ddd02bd60ddb8aee17857fcf3265c41f578b373975e5c221549c1dfa83c407207ee180ce65adaa3f8847106fc8f3a692159e12ac0904b195ce9674338cc4c8b9bec6053ee7f33946da220d5f725ba930ce3175a09d4e8739b98b624517a35ee656dc14d6407fb581edd92bde8bb273bb587d8ffdad1519b143ad0930ce95123ed89674a17ba14645a2d6ca0b95d47f63a43b6d9afaeda208539821fa4e8bd52f5f5ea842e98f8801e551f8d3a82e014bbf09c17beba206b6bb143fe349a2161dda1f20d855d382ad30373abe4d4b765db6d396adcbda3fbf9038d97e44e0a5784b8d68b6deaddbe71b96530b37e01e13aeab349b797fb65a25faa51701fb6bf1b184c269180e9888bc720f9ec73d4f381693b72b87e1e4a03e45c85e346da4d0191d330d6483cc4e43d98d51bd01e4dea9b00575d1d9206ba4780e4eabbf3ac9d1f71543cdbc930b39df1fb4607f7419a6cb3876cc17700e849e2fc59d7c91736bba46f048d080bf7ffa826f2f44ba1e7301a90af03323eec5431852b13b55d54a15f299f0711579bcd006b8f334559498c48c3403138ef696526551a9d975ec858142ab9fb938cbb2ab791e1552119fb2710804672f4ef66ef126e0c185c94e1014d8db3c88e1c57e4a91c7fe9a58d758e46d21038667c72e6ffa24c19c59a51a17db6ada0099a33d54aa880c681ca90f91cb25991f807c1a8d2e9cc010c85821a20a9aa3658066aa6ce33f5394bdcd289a016e0b4e209b1ff84ecd6c0a5c2c7ff6e40598d1f5898ffe57f24d7c2fcee9910d9c1160e54f9b47f0ddf16b516aaddbb1e6e7752e32d95edc86ca02ca063b1cf02c2993f0541eb630709a8b796f5aec9cca499b995965a6027b9933455c557c238ded3935da491dcd0629c8919582fb6e449f83b2b5ee4d0102fce296adf76e97114f6de61baa2823510b84dce294583c4e8cd09e5176f1239bea27287b62dac1da1809735704a5305e00478cd96b5ee302a42e4e899b02ccc9ff0670a036db5598323273d4bdc7332695fbeec0b02ea6bee58455416cc8c2014092b28d52899e7eeb239efaab147e178d4056f7c7b6c1d9b24248d65dba05b459cf32d0d7dcb88298e25ba1a1130646b8bb772c5fbff425f87ccf3a2e43a375a942b282a4760ec8d4252870768825169aa0baf3c921ca32786753c0457ef803756162102c72129fcf64b4eb7c11c279687ca42e26723cb1d34f3beadb6097e9e60a59a9b
    // 19 76a914dc4d0fbb777fb5652a5ecdaaa7c5a1e4c143b48288ac
    // 031a548b9e716bedab44b3d5ce195de945380ef45e79771ec5d1d89ddbbfd61335fd040a401ffa18dbd19e1bfecd96b88dac61da9ea80ad4d39c6960a80ab8a20aa03340953235d578b4e36f91ade8b34e49ca08d51ba7591aa6fbe9c23d8d16131dd898e67fef10814f32ae6fa0c445fd53d68c09dcb3d9ee51b58b5667dd0147d768b1a56ed35f0d9e58087af310594f2c96d03859954e209e0a68d7dc27a76afb2c01dc4bfd5c433cfd829b611fb471369f2e547b6525f27e31935a7ce2448a7f39419aa0d7ab84268628f6fc6cf6a205e898313f4a51303821bfad92fe1563f5afb71f9bf53addede3101c66706c273942e9d7f30355f43cbd1a7c15858e7130a96bfb7b47387f4048a651d5bc9a315ce02435a3e022511d6623b995d1505a69726ddc4f23b1ad5690b488f0b83169f60b8c5d0df8dc2de6f3534df4bbb4b64d2be88fd060f0645686912f7242cb1f296b60ddbc5c711ed097b18e959d2011a5fd79fcb41bcc925ed542f5d664374bca60404b562c2fde0b2e76aa405812a1fca4ba5fdd9d75a21e696027068e7ae4da38b934c987f219e4f80ba05b67ece90116128f2380396d2ee15b129fa300023fd36e5486dbc6ed811b50b3e718100da68b7ab4fd5a01080733596e05e151ad47c8b52e8ee7688f3f68c115133b27208d6efa4f182430880e202c58458672adcfe17e13066c2a57ef6ed5a9350d94e98f146cf53ecc8462436d11fdf937443882376e94b1fe605c2a30a0854b93796bd83ea0be3a4487cc6c941b4f886f1e795e9d026ba1b109d78c3977755ff222ed984e2f943d6ca42ff00b770954ae0c80809082b6e09d324c5b1fb690e72d9bcca52a3b1abd67c4670a7fdff815fb3b0fc641be86a525027525ad6c1f22098a6d448598327fc00d48b00f6db6f48b5436a4d35ab78d4ce834f034f66ddf9efbdd7c83f15f4ef2af3c1c0063b40f8d1f9312cb926a77fc5a91fa6b5717271cbc967522b4eca5af7aa9b52ac78ba0d14791a4aa17e9f1ecb1a6f5e7fb60b351680f4604e3522c471fd306b4227c1f33ceb40e7e3d6f37cb2d4553f4fb46413f74f8ce5bae4c70e97116ce951e28b4d4d414fd0e7472ce911d9cc5c37aeabb02717ac6c245d6cc6817896a7bda9b7ed69f6bd8c596a6be4ae0e10c2d39bb8e881ca5d4782b32ac375ac82c3cee3547486b0bb7647fec155c1fc5467ee188fc81f06fbfdee3593d957ceeffc9af3fad0d0ef60ca8b76bbbdbb0729dcf9312fe4c4fdb494f60a872a280f7b9ed7ee55e5c34a566cf7d8e9af8a6bd59d3f5f4411a85d14aac081588dca0c923ca5b638214ef990b1e60fda998934b14ea8bbf8c7d47e44c88e609e7db0a1eefc26147284d2f5c85317017f7c1168b9ef0e2cf80051105d7e93d19460ff7c7268612c9f02351afdf07c8c7d6b16e840a2cdb52f013c98fa6c95b54ee0165dfc959116ca156017b6c1c7b91b242943bb49f83c6ae04350ca8e6208ac07f9b7890e51da8d46bd855f9fecb1f2b13b2398b1c37f5f34b932cdc5104ab71942bcbb98ed05cbb0c0622ef4626ad2dde76a038b7cb9d4cb830e17bc5082a3cd5a3d59077064f1fca9abcfa73049c6cd8aafa00481ecfb058a3cc8b7ba094f39cdddaf5acddbb4346251a00be42eb88c7da19975951ab38899ad36aa7d64984ce6860e73f3c9141fa5f215cc2c5cdd4732125328d84c59739d05c32352930352efa1d9c2901bf954c510d758ea380541ff6612b5357ce6cea342b7d5f82dab18f2e231067d4219e44cf9cca7514a6aa0f966dd3bddd4563a13c7093769bbb449b5f744c051d9f5488f6721c248fbd5a420e8870ecadfcd4fb0450dd1162d28ad7a6943fb3da242ceafdc85c233f9e3968dd227bcd725d00e2926dd31e8e9d2a412231c9da131760209fcbffe9b7c15dc807d16061082ba0568dd819bf9724238c4310bd36f3d8899a1cadc9d1e6ef164176ad2f1c98f6d5ce43db2c9322a6eca7f8bff1a1b2c18d3d84ca86a200ae4c513756adce8b3879db4f71570b60c9c53c48a7dd4387c757160fc8c7396072999b9d2006407942fafed14f2c32a9970357f309fba3f80aa2c742fc4fbfbfbc57e6be89226e795d77544a7e1a0bc66a9279228cc7ccd4c0b34cb205c2aa4e654988c17cab936bcf2887816a430f788c6452924119b8cf1d0ef317c06c589b5a9fcfa23292f4164ae80aee90c6f241c8b1389f56e657170be376bceaa9dd514d9228428d13d3f245be8721e47f7a59eda52c5e72a8b3713c0eea714dfaa4b36c4692df4f8abd65136863e0143d081f92415ba5a72f03f2f1b2edff5569c17783dcca11b67c77fc2c8494692ec7edfb38bacf4efee4c1e7290244831f3a33572c121c581a9123c06aa6929233754d3162781f98b3d73cd4db168e2905f805730c527c39e1b566aade73fda45cde9e74687a2298f6de6df577416863d21b52a35248b471fab4a56f16f102fa7d0d3303a616edbeffbad3687833ab858a8c146996fb5ee0301331af9f41f81071a93d90b1663404f58766d34d427519add1fd8cbd9821343bfa40eee1a768d7333f97dbe7e513a0aef06d7b8e95c414cb83e5376c1cdf2c644d6307ff151d3651af4f96e5a0d01d31060664362751c44357f95f9c8962d874d7b4fa3e50876093c06be158cde7655c13a1e6c4feb64574bd7d5b6a32c3aaceb1651534b30691a8b8dc380f139cbcaabfaf76a1a98a72ac54703a2bf2bd7fd8b1b1a8e301383ed7a07d9adbe30c41b4ded6ff63e78df313fec6a4aae852b3a729b8d6029ed1c03a8db1f0c374934be861746319fb32f91fb3fc198fca322959822167d6d0ebae12f3eed742338cf11f540ac23076474fac5773c0ebfbad300b0ba7df7fdd76b61ae1b1b3903941ddbf16fbef1b29d274e55246166ca14a5d81d3c9527fbce7b2d0bc97356f1873c9b9b010d0263aa6edc9c3271a003b88c98e2ae6c9c9b8fb62a64541610be251b14a95fa36e30ce205996294ae6d5d16c0cd551ff6971ee52855b5bee5c87422c7558e424255168497d0c6f556fbe72aff68479afad1ba6d8a5bbed101471caffb694e2213a7646afd238b5f47aa694c4ba443710b9c417d1eb445f731ed1fa6c7cd3583be7071a1ed836b2fdcf6d12d3d1cb5dc4dbca8f11ebc870d8b6f07e407fd9efd87a0630de9765305eb0ca7f42155ff4b67a3560cfee19147e38da1c8a28d6e0bb9d68474539658ad99b324a3629e4d708265bbe0f8564150640ce8d4706dc4b0c0b334c81cd002bfee065823b3afb74912dc7821efcdd8575f3c8739fe262a19635d379929774313ceff2f990052ec8b346330215e7dc6891ec398ef68563ff83e0b68a57f4a68954acf1a1e9feae0955d6ad7f21d1070089d089df8ddc9b4e4dbde7722c1cf1ac66448c330c80378869940b890016d12e3a6881d8c8926fd404cd51b126d51c55d3e13c1bc567ca7f3958230e49218eeabfd622fec183ae04d186f57e21283a4a373d0c8959ad04bd3df4a058d778f6301d49b6d4bcafec61a244a41ec102f505062b47eef75064ab38d0dd38565a34fc8d3b70383113583c5ef5b27994c81a51c4f948da220cb9c9f41dca809410d8f27cdda9aefd4f7ff498da28eaf8086daeefbc65854110c623c6bb125e0fbaaf98d59d1ae4fbf210389d847ec8bf6e6d045df268f5c651987ce6fd05c0fdcdc47f7c3b768a0f86104
    // 19 76a9140223e5ca4c89673e039dc8d01b8b1d5b5039629788ac
    // 00000000
    // 4 byte version
    // 1 byte, 1 input
    // aa87-e5ce, 32 byte prev tx
    // outpoint idx
    // script length (64)
    // script (pushdata x2)
    // sequence
    // explicit fee
    // output size (2x)
    // 'serValue' (the 2.5k thing?)
    // scriptPubKey size
    // script (P2PKH)
    // 'serValue' #2
    // scriptPubKey #2 size
    // script (P2PKH)
    // locktime
    // ...

    pointer = parseTransactionFee(transaction, pointer, bytes);



    // Parse the transaction output size
    pointer = parseTransactionOutputSize(transaction, pointer, bytes);

    System.out.println("Output size: " + transaction.getOutputSize().getValue());

    // Parse the transaction outputs
    pointer = parseTransactionOutputs(transaction, pointer, bytes);

    // Parse the lock time
    pointer = parseLockTime(transaction, pointer, bytes);

    // Compute the transaction tx
    computeTransactionHash(transaction, initialPointer, pointer, bytes);

    // Verify if the byte array size is equal to the pointer
    // TODO This will not working in the future because it'd be possible that a massive byte array which doesn't
    // represent a single transaction is passed (such as blocks)
    if(pointer != bytes.length) {
      throw new IllegalStateException("Raw transaction bytes not fully consumed");
    }

    return transaction;
  }

  private static void computeTransactionHash(final Transaction transaction, final int initialPointer, final int pointer, final byte[] bytes) {
    final byte[] txBytes = ArrayUtil.arrayCopy(bytes, initialPointer, pointer);

    // Create SHA256 digest and feed it the tx bytes
    final byte[] txHash = ComputeUtil.computeDoubleSHA256(txBytes);

    // Convert to LE
    ArrayUtil.reverse(txHash);

    // Set the transaction tx
    transaction.setTransactionId(txHash);
  }

  private static int parseTransactionInputs(final Transaction transaction, final int initialPointer, final byte[] bytes) {
    int pointer = initialPointer;

    final long numInputs = transaction.getInputSize().getValue();
    final ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>((int) numInputs);

    // Stick it all in the transaction
    transaction.setInputs(inputs);

    // Iterate over the number of inputs in the transaction
    for (int i = 0; i < numInputs; i++) {
      // Create an empty transaction input
      final TransactionInput input = new TransactionInput();

      // Set the index
      input.setInputIndex(i);

      // Add the input to the list (early, to be able to see from where it went wrong, if it goes wrong)
      inputs.add(input);

      // Parse the tx out point to the previous transaction
      pointer = parseTransactionOutPoint(input, pointer, bytes);

      // Parse the script
      pointer = ScriptParseUtil.parseScript(input, pointer, bytes, transaction.isCoinbase());

      // Parse the sequence bytes
      pointer = parseSequence(input, pointer, bytes);
    }

    return pointer;
  }

  private static int parseTransactionOutputs(final Transaction transaction, final int initialPointer, final byte[] bytes) {
    int pointer = initialPointer;

    final long numOutputs = transaction.getOutputSize().getValue();
    final ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>((int) numOutputs);

    // Stick it all in the transaction
    transaction.setOutputs(outputs);

    // Iterate over the number of output in the transaction
    for (int i = 0; i < numOutputs; i++) {
      // Create an empty transaction output
      final TransactionOutput output = new TransactionOutput();

      // Set the index
      output.setOutputIndex(i);

      // Add the output to the list (early, to be able to see from where it went wrong, if it goes wrong)
      outputs.add(output);

      // Parse the transaction value
      System.out.println("parsing tx output " + i);
      if(transaction.isCoinbase()) {
        output.setTransactionValue(ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + COINBASE_OUTPUT_VALUE_SIZE));
      } else {
        output.setTransactionValue(ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + TRANSACTION_OUTPUT_VALUE_SIZE));
      }

      // Parse the script
      pointer = ScriptParseUtil.parseScript(output, pointer, bytes, false);
    }

    return pointer;
  }

  private static int parseTransactionOutPoint(final TransactionInput input, final int initialPointer, final byte[] bytes) {
    int pointer = initialPointer;

    final TransactionOutPoint outPoint = new TransactionOutPoint();

    final byte[] prevHash = ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + TRANSACTION_INPUT_OUTPOINT_SIZE);
    outPoint.setReferenceTransaction(prevHash);

    outPoint.setIndex(NumberParseUtil.parseUint32(ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + TRANSACTION_OUTPOINT_INDEX_SIZE)));

    input.setOutPoint(outPoint);

    return pointer;
  }

  private static int parseTransactionInputSize(final Transaction transaction, final int pointer, final byte[] bytes) {
    final VariableLengthInteger variableInteger = new VariableLengthInteger(bytes, pointer);
    transaction.setInputSize(variableInteger);
    return pointer + variableInteger.getByteSize();
  }

  private static int parseTransactionFee(final Transaction transaction, final int initialPointer, final byte[] bytes) {
    int pointer = initialPointer;
    NumberParseUtil.parseLong(ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + TRANSACTION_FEE_VALUE_SIZE));
    return pointer;
  }

  private static int parseTransactionOutputSize(final Transaction transaction, final int pointer, final byte[] bytes) {
    final VariableLengthInteger variableInteger = new VariableLengthInteger(bytes, pointer);
    transaction.setOutputSize(variableInteger);

    return pointer + variableInteger.getByteSize();
  }

  private static int parseSequence(final TransactionInput input, final int initialPointer, final byte[] bytes) {
    int pointer = initialPointer;
    input.setTransactionSequence(NumberParseUtil.parseUint32(ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + TRANSACTION_SEQUENCE_SIZE)));
    return pointer;
  }

  private static int parseVersion(final Transaction transaction, final int initialPointer, final byte[] bytes) {
    int pointer = initialPointer;
    transaction.setVersion(NumberParseUtil.parseUint32(ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + TRANSACTION_VERSION_FIELD_SIZE)));
    return pointer;
  }

  private static int parseLockTime(final Transaction transaction, final int initialPointer, final byte[] bytes) {
    int pointer = initialPointer;
    transaction.setLockTime(NumberParseUtil.parseUint32(ArrayUtil.arrayCopy(bytes, pointer, pointer = pointer + TRANSACTION_LOCK_TIME_SIZE)));
    return pointer;
  }
}
