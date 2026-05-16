# Mock API Documentation

The application currently uses an offline-first mock API strategy:

1. `LocalAssetProjectLoader` reads `app/src/main/assets/projects.json`.
2. `OfflineFirstProjectRepository` upserts the data into Room.
3. ViewModels observe Room with Kotlin Flow so cached data remains available offline.

## Future Retrofit Endpoint

`ProjectApiService` defines the contract for replacing local JSON with a backend service:

```http
GET /projects
Accept: application/json
```

## Response Shape

```json
[
  {
    "id": 1,
    "title": "Main Road Repair",
    "description": "Repair and resurfacing of the main village road...",
    "budget": 1500000,
    "startDate": "2026-02-10",
    "expectedCompletion": "2026-08-10",
    "progress": 65,
    "status": "In Progress",
    "department": "Rural Development and Panchayat Raj Department",
    "contractor": "Sri Lakshmi Infrastructure",
    "budgetUtilization": 975000,
    "imageUrl": "https://example.org/road.jpg",
    "beforeImages": ["https://example.org/before.jpg"],
    "afterImages": ["https://example.org/after.jpg"],
    "updates": ["Foundation completed", "Materials delivered"]
  }
]
```

## Status Values

- `Not Started`
- `In Progress`
- `Completed`
- `Delayed`
