using UnityEngine;
using System.Collections;

public class TogglePanel : MonoBehaviour {

    public GameObject toggled;

    public void toggle() {
        toggled.SetActive(!toggled.activeSelf);
    }
}
