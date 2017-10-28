using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace project.sarica.ann
{
    public class NeuronSphere : MonoBehaviour
    {
        public int neuronCount = 10;
        public Transform neuronPrefab;

        private Transform[] neurons;

        private void Awake()
        {
            neurons = new Transform[neuronCount];
            for (int i = 0; i < neuronCount; i++)
            {
                neurons[i] = Instantiate(neuronPrefab) as Transform;
                neurons[i].transform.SetParent(transform);
            }

            SpreadNeurons();
        }

        private void SpreadNeurons()
        {
            int population = 100;
            float epsilon = 0.0001f;

            Vector3[,] pos = new Vector3[population, neurons.Length];
            for (int i = 0; i < population; i++)
                for (int j = 0; j < neurons.Length; j++)
                   pos[i,j] = Random.onUnitSphere;


            float[] scores = new float[population];
            float lastAverage = -1;
            int winner = 0;
            while (true)
            {
                float avg = 0f;
                float best = 0f;
                for (int a = 0; a < population; a++)
                {
                    scores[a] = 0f;
                    for (int i = 0; i < neurons.Length; i++)
                        for (int j = 0; j < neurons.Length; j++)
                            scores[a] += Vector3.Distance(pos[a, i], pos[a, j]);
                    avg += scores[a];

                    if (scores[a] > best)
                    {
                        best = scores[a];
                        winner = a;
                    }
                }
                avg /= population;
                if (avg - lastAverage < epsilon)
                    break;
                lastAverage = avg;

                for (int i = 0; i < population; i++)
                    if (scores[i] < avg)
                        scores[i] = 0f;

                for (int i = 0; i < population; i++)
                {
                    if (scores[i] > 0f)
                        continue;

                    int parent = 0;
                    do
                    {
                        parent = Random.Range(0, population);
                        if (scores[parent] > 0)
                            break;
                    }
                    while (true);

                    for (int j = 0; j < neurons.Length; j++)
                    {
                        pos[i, j] = pos[parent, j];
                        pos[i, j].x += Random.value * 0.2f - 0.1f;
                        pos[i, j].y += Random.value * 0.2f - 0.1f;
                        pos[i, j].z += Random.value * 0.2f - 0.1f;
                        pos[i, j].Normalize();
                    }
                }
            }

            for (int i = 0; i < neurons.Length; i++)
                neurons[i].localPosition = pos[winner, i];
        }

        private void OnValidate()
        {
            neuronCount = Mathf.Max(1, neuronCount);
        }
    }
}
