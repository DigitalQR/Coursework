using UnityEngine;
using System.Collections;

public class SceneManager : MonoBehaviour {

    public void switchScene(string scene) {
        Application.LoadLevel(scene);
    }
}
