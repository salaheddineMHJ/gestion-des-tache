

# Task Management System (Gestion de Tâches)

![JavaFX](https://img.shields.io/badge/JavaFX-17-blue) ![License](https://img.shields.io/badge/license-MIT-green)

This project is a **Task Management System** built using JavaFX. It allows users to manage tasks efficiently by adding, modifying, filtering, and viewing tasks. The system provides a user-friendly graphical interface with features like task prioritization, categorization, reminders, and progress tracking.

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Prerequisites](#prerequisites)
4. [Installation](#installation)
5. [Usage](#usage)
   - [Main Dashboard](#main-dashboard)
   - [Adding and Modifying Tasks](#adding-and-modifying-tasks)
   - [Viewing Messages and Diagrams](#viewing-messages-and-diagrams)
6. [Code Structure](#code-structure)
7. [Contributing](#contributing)
8. [License](#license)

---

## Overview

The **Task Management System** is designed to help users organize their tasks effectively. It includes:

- A dashboard summarizing task statuses (e.g., in-progress, urgent, completed).
- A table view displaying detailed task information.
- Forms for adding and modifying tasks.
- Filters and search functionality for easy task management.
- Visualizations such as progress bars and diagrams.

This application is built using **JavaFX**, ensuring a modern and responsive user interface.

---

## Features

### Core Features
- **Task Management**:
  - Add, modify, and delete tasks.
  - Assign priorities, categories, and due dates to tasks.
  - Set reminders for upcoming tasks.
- **Dashboard**:
  - View summaries of tasks in progress, urgent tasks, and overall progress.
  - Progress bar indicating global task completion.
- **Search and Filter**:
  - Search tasks by title or description.
  - Filter tasks by category.
- **Task Visualization**:
  - View tasks in table format or as cards.
  - Display tasks categorized as "Upcoming" or "Completed."
- **Messages and Diagrams**:
  - View system-generated messages.
  - Generate diagrams for task analysis.

---

## Prerequisites

To run this project, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 17 or higher.
- **JavaFX SDK**: Required for running JavaFX applications.
- **IDE**: IntelliJ IDEA, Eclipse, or any IDE supporting JavaFX.
- **Git**: To clone the repository (optional).

---

## Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/task-management-system.git
   cd task-management-system
   ```

2. **Set Up JavaFX**:
   - Download the JavaFX SDK from [Gluon's website](https://gluonhq.com/products/javafx/).
   - Configure your IDE to include the JavaFX libraries. For example, in IntelliJ IDEA:
     - Go to `File > Project Structure > Libraries`.
     - Add the JavaFX SDK path.

3. **Build the Project**:
   - If using an IDE, import the project and build it directly.
   - If using the command line:
     ```bash
     javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d out src/**/*.java
     ```

4. **Run the Application**:
   ```bash
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp out gestion_de_tâches.Main
   ```

---

## Usage

### Main Dashboard

Upon launching the application, you will see the main dashboard, which includes:
- **Task Summary**:
  - Number of tasks in progress.
  - Number of urgent tasks.
  - Global progress bar.
- **Task Table**:
  - Displays all tasks with details such as title, description, due date, priority, category, and status.
- **Buttons**:
  - Add, modify, or delete tasks.
  - View messages, diagrams, upcoming tasks, and completed tasks.

### Adding and Modifying Tasks

1. **Add a Task**:
   - Click the "Ajouter" button.
   - Fill in the form fields (title, description, due date, priority, category, reminder days).
   - Save the task.

2. **Modify a Task**:
   - Select a task from the table.
   - Click the "Modifier" button.
   - Update the task details and save.

### Viewing Messages and Diagrams

- **Messages**:
  - Click the "Messages" button to view system-generated notifications.
- **Diagrams**:
  - Click the "Diagramme" button to generate visual representations of task data.

---

## Code Structure

The project is organized into the following components:

- **FXML Files**:
  - Define the layout and structure of the UI components.
  - Located in the `src/main/resources/fxml` directory.
- **Controllers**:
  - Handle user interactions and logic for each UI component.
  - Located in the `gestion_de_tâches` package.
- **Models**:
  - Represent the data structures for tasks, messages, and other entities.
- **CSS Stylesheets**:
  - Customize the appearance of the UI.
  - Located in the `src/main/resources/styles` directory.


---

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

