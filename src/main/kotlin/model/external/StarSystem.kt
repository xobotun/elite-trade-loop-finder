package com.xobotun.elite_trade_loop_finder.model.external

/**
 * Populated system data class
 * Download via https://eddb.io/archive/v6/systems_populated.json
 */
data class StarSystem (
    val id: Long,
    val name: String,
    val x: Double,
    val y: Double,
    val z: Double,
)

/*
Raw json example below

{
  "id": 1,
  "edsm_id": 12695,
  "name": "1 G. Caeli",
  "x": 80.90625,
  "y": -83.53125,
  "z": -30.8125,
  "population": 6544826,
  "is_populated": true,
  "government_id": 144,
  "government": "Patronage",
  "allegiance_id": 2,
  "allegiance": "Empire",
  "states": [
    {
      "id": 80,
      "name": "None"
    }
  ],
  "security_id": 32,
  "security": "Medium",
  "primary_economy_id": 4,
  "primary_economy": "Industrial",
  "power": "Arissa Lavigny-Duval",
  "power_state": "Exploited",
  "power_state_id": 32,
  "needs_permit": false,
  "updated_at": 1606421818,
  "simbad_ref": "",
  "controlling_minor_faction_id": 31816,
  "controlling_minor_faction": "1 G. Caeli Empire League",
  "reserve_type_id": 3,
  "reserve_type": "Common",
  "minor_faction_presences": [
    {
      "happiness_id": 2,
      "minor_faction_id": 31816,
      "influence": 54.7904,
      "active_states": [
        {
          "id": 80,
          "name": "None"
        }
      ],
      "pending_states": [],
      "recovering_states": []
    },
    {
      "happiness_id": 2,
      "minor_faction_id": 54517,
      "influence": 2.3952,
      "active_states": [
        {
          "id": 80,
          "name": "None"
        }
      ],
      "pending_states": [],
      "recovering_states": []
    },
    {
      "happiness_id": 2,
      "minor_faction_id": 54518,
      "influence": 4.1916,
      "active_states": [
        {
          "id": 80,
          "name": "None"
        }
      ],
      "pending_states": [],
      "recovering_states": []
    },
    {
      "happiness_id": 2,
      "minor_faction_id": 54519,
      "influence": 6.0878,
      "active_states": [
        {
          "id": 80,
          "name": "None"
        }
      ],
      "pending_states": [],
      "recovering_states": []
    },
    {
      "happiness_id": 2,
      "minor_faction_id": 74917,
      "influence": 10.479,
      "active_states": [
        {
          "id": 80,
          "name": "None"
        }
      ],
      "pending_states": [],
      "recovering_states": []
    },
    {
      "happiness_id": 2,
      "minor_faction_id": 40897,
      "influence": 12.6747,
      "active_states": [
        {
          "id": 80,
          "name": "None"
        }
      ],
      "pending_states": [],
      "recovering_states": []
    },
    {
      "happiness_id": 2,
      "minor_faction_id": 61626,
      "influence": 9.3812,
      "active_states": [
        {
          "id": 80,
          "name": "None"
        }
      ],
      "pending_states": [],
      "recovering_states": []
    }
  ],
  "ed_system_address": 560249948515
}

 */
