package one.block.eosiojavaabieosserializationprovider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the JSON templates for ABI conversion as immutable static Maps.  These are used
 * to serialize/deserialize specific types.
 */
public class AbiEosJson {
    public static final Map<String, String> abiEosJsonMap = initAbiEosJsonMap();

    /**
     * Initialize an immutable static map with the JSON templates for ABI and Transaction
     * serialization and deserialization.  These are used as inputs to the ABIEOS C++
     * serialization provider implementation.
     * @return - Immutable map containing the ABI and Transaction JSON serilization/deserialization templates.
     */
    private static Map<String, String> initAbiEosJsonMap() {
        Map<String, String> jsonMap = new HashMap<>();

        jsonMap.put("eosio.assert.abi.json", "{\n" +
                "   \"version\": \"eosio::abi/1.0\",\n" +
                "   \"structs\": [\n" +
                "      {\n" +
                "         \"name\": \"chain_params\",\n" +
                "         \"base\": \"\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"chain_id\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"chain_name\",\n" +
                "               \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"icon\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"stored_chain_params\",\n" +
                "         \"base\": \"\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"chain_id\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"chain_name\",\n" +
                "               \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"icon\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"hash\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"next_unique_id\",\n" +
                "               \"type\": \"uint64\"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"contract_action\",\n" +
                "         \"base\": \"\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"contract\",\n" +
                "               \"type\": \"name\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"action\",\n" +
                "               \"type\": \"name\"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"manifest\",\n" +
                "         \"base\": \"\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"account\",\n" +
                "               \"type\": \"name\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"domain\",\n" +
                "               \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"appmeta\",\n" +
                "               \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"whitelist\",\n" +
                "               \"type\": \"contract_action[]\"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"stored_manifest\",\n" +
                "         \"base\": \"\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"unique_id\",\n" +
                "               \"type\": \"uint64\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"id\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"account\",\n" +
                "               \"type\": \"name\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"domain\",\n" +
                "               \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"appmeta\",\n" +
                "               \"type\": \"string\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"whitelist\",\n" +
                "               \"type\": \"contract_action[]\"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"del.manifest\",\n" +
                "         \"base\": \"\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"id\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            }\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"require\",\n" +
                "         \"base\": \"\",\n" +
                "         \"fields\": [\n" +
                "            {\n" +
                "               \"name\": \"chain_params_hash\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"manifest_id\",\n" +
                "               \"type\": \"checksum256\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"actions\",\n" +
                "               \"type\": \"contract_action[]\"\n" +
                "            },\n" +
                "            {\n" +
                "               \"name\": \"abi_hashes\",\n" +
                "               \"type\": \"checksum256[]\"\n" +
                "            }\n" +
                "         ]\n" +
                "      }\n" +
                "   ],\n" +
                "   \"actions\": [\n" +
                "      {\n" +
                "         \"name\": \"setchain\",\n" +
                "         \"type\": \"chain_params\",\n" +
                "         \"ricardian_contract\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"add.manifest\",\n" +
                "         \"type\": \"manifest\",\n" +
                "         \"ricardian_contract\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"del.manifest\",\n" +
                "         \"type\": \"del.manifest\",\n" +
                "         \"ricardian_contract\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"require\",\n" +
                "         \"type\": \"require\",\n" +
                "         \"ricardian_contract\": \"\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"tables\": [\n" +
                "      {\n" +
                "         \"name\": \"chain.params\",\n" +
                "         \"type\": \"stored_chain_params\",\n" +
                "         \"index_type\": \"i64\",\n" +
                "         \"key_names\": [\n" +
                "            \"key\"\n" +
                "         ],\n" +
                "         \"key_types\": [\n" +
                "            \"uint64\"\n" +
                "         ]\n" +
                "      },\n" +
                "      {\n" +
                "         \"name\": \"manifests\",\n" +
                "         \"type\": \"stored_manifest\",\n" +
                "         \"index_type\": \"i64\",\n" +
                "         \"key_names\": [\n" +
                "            \"key\"\n" +
                "         ],\n" +
                "         \"key_types\": [\n" +
                "            \"uint64\"\n" +
                "         ]\n" +
                "      }\n" +
                "   ],\n" +
                "   \"ricardian_clauses\": [],\n" +
                "   \"abi_extensions\": []\n" +
                "}");

        jsonMap.put("transaction.abi.json", "{\n" +
                "    \"version\": \"eosio::abi/1.0\",\n" +
                "    \"types\": [\n" +
                "        {\n" +
                "            \"new_type_name\": \"account_name\",\n" +
                "            \"type\": \"name\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"new_type_name\": \"action_name\",\n" +
                "            \"type\": \"name\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"new_type_name\": \"permission_name\",\n" +
                "            \"type\": \"name\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"structs\": [\n" +
                "        {\n" +
                "            \"name\": \"permission_level\",\n" +
                "            \"base\": \"\",\n" +
                "            \"fields\": [\n" +
                "                {\n" +
                "                    \"name\": \"actor\",\n" +
                "                    \"type\": \"account_name\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"permission\",\n" +
                "                    \"type\": \"permission_name\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"action\",\n" +
                "            \"base\": \"\",\n" +
                "            \"fields\": [\n" +
                "                {\n" +
                "                    \"name\": \"account\",\n" +
                "                    \"type\": \"account_name\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"name\",\n" +
                "                    \"type\": \"action_name\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"authorization\",\n" +
                "                    \"type\": \"permission_level[]\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"data\",\n" +
                "                    \"type\": \"bytes\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"extension\",\n" +
                "            \"base\": \"\",\n" +
                "            \"fields\": [\n" +
                "                {\n" +
                "                    \"name\": \"type\",\n" +
                "                    \"type\": \"uint16\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"data\",\n" +
                "                    \"type\": \"bytes\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"transaction_header\",\n" +
                "            \"base\": \"\",\n" +
                "            \"fields\": [\n" +
                "                {\n" +
                "                    \"name\": \"expiration\",\n" +
                "                    \"type\": \"time_point_sec\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"ref_block_num\",\n" +
                "                    \"type\": \"uint16\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"ref_block_prefix\",\n" +
                "                    \"type\": \"uint32\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"max_net_usage_words\",\n" +
                "                    \"type\": \"varuint32\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"max_cpu_usage_ms\",\n" +
                "                    \"type\": \"uint8\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"delay_sec\",\n" +
                "                    \"type\": \"varuint32\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"transaction\",\n" +
                "            \"base\": \"transaction_header\",\n" +
                "            \"fields\": [\n" +
                "                {\n" +
                "                    \"name\": \"context_free_actions\",\n" +
                "                    \"type\": \"action[]\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"actions\",\n" +
                "                    \"type\": \"action[]\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"name\": \"transaction_extensions\",\n" +
                "                    \"type\": \"extension[]\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}");
        return Collections.unmodifiableMap(jsonMap);
    }
}
