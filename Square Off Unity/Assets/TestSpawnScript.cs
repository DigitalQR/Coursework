using UnityEngine;
using System.Collections;

public class TestSpawnScript : MonoBehaviour {

    public GameObject player;

    private bool[] players = new bool[5];

	// Update is called once per frame
	void Update () {
        string[] lookups = {
            "Keyboard ",
            "Gamepad 1 ",
            "Gamepad 2 ",
            "Gamepad 3 ",
            "Gamepad 4 ",
        };

        for (uint i = 0; i < 5; i++) {
            if (Input.GetAxis(lookups[i] + "Jump") != 0 && !players[i]) {
                GameObject new_player = Instantiate(player);
                new_player.GetComponent<PlayerController>().colour = new Color(Random.Range(0f, 1f), Random.Range(0f, 1f), Random.Range(0f, 1f));

                if (i == 0)
                {
                    new_player.GetComponent<PlayerController>().keyboard_controlled = true;
                }
                else
                {
                    new_player.GetComponent<PlayerController>().keyboard_controlled = false;
                    new_player.GetComponent<PlayerController>().controller_id = i;
                }

                players[i] = true;
            }
        }
	}
}
