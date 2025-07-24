package proy25;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;

public class x extends javax.swing.JFrame {

    private SistemaGestionActividades sistema;

    private JButton addActivityButton;
    private JButton addPersonButton;
    private JButton enrollButton;
    private JButton viewActivitiesButton;
    private JButton viewPersonsButton;
    private JButton viewEnrollmentsButton;
    private JButton loadDataButton;
    private JButton updateDataButton;
    private JTextArea displayArea;

    private JDialog addPersonDialog;
    private JTextField personNameField;
    private JTextField personEmailField;
    private JComboBox<String> personTypeComboBox;
    private JTextField institutionField;
    private JTextField ageField;
    private JTextField genderField;
    private JButton submitAddPersonButton;
    private JLabel institutionLabel;
    private JLabel ageLabel;
    private JLabel genderLabel;

    private JDialog addActivityDialog;
    private JComboBox<String> activityTypeComboBox;
    private JTextField activityTitleField;
    private JTextField activityDescriptionField;
    private JTextField activityStartDateField;
    private JTextField activityEndDateField;
    private JTextField activityLocationField;
    private JTextField activityOrganizerIdField;
    private JTextField activityCapacityField;
    private JTextField activitySessionsField;
    private JCheckBox activityPublicCheckbox;
    private JCheckBox activityMaterialsCheckbox;
    private JButton submitAddActivityButton;

    private JDialog enrollDialog;
    private JTextField enrollParticipantIdField;
    private JTextField enrollActivityIdField;
    private JButton submitEnrollButton;


    public x() {
        initComponents();
        sistema = new SistemaGestionActividades();
        setupGUI();
        addEventHandlers();
        pack();
        setLocationRelativeTo(null);
    }

    private void setupGUI() {
        getContentPane().setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(30, 30, 60));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        menuPanel.setBackground(new Color(50, 50, 90));
        menuPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        addActivityButton = new JButton("1. Añadir Actividad");
        addPersonButton = new JButton("2. Añadir Persona");
        enrollButton = new JButton("3. Inscribir en Actividad");
        viewActivitiesButton = new JButton("4. Ver Actividades");
        viewPersonsButton = new JButton("5. Ver Personas");
        viewEnrollmentsButton = new JButton("6. Ver Inscripciones");
        loadDataButton = new JButton("7. Cargar datos DB");
        updateDataButton = new JButton("8. Actualizar DB");

        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);
        Color buttonBg = new Color(70, 130, 180);
        Color buttonFg = Color.WHITE;

        JButton[] buttons = {addActivityButton, addPersonButton, enrollButton,
                             viewActivitiesButton, viewPersonsButton, viewEnrollmentsButton,
                             loadDataButton, updateDataButton};

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(buttonBg);
            btn.setForeground(buttonFg);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(120, 200, 255), 3, true));
        }

        menuPanel.add(addActivityButton);
        menuPanel.add(addPersonButton);
        menuPanel.add(enrollButton);
        menuPanel.add(viewActivitiesButton);
        menuPanel.add(viewPersonsButton);
        menuPanel.add(viewEnrollmentsButton);
        menuPanel.add(loadDataButton);
        menuPanel.add(updateDataButton);

        getContentPane().add(menuPanel, BorderLayout.NORTH);

        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setBackground(new Color(20, 20, 40));
        displayArea.setForeground(new Color(170, 220, 255));
        displayArea.setCaretColor(new Color(170, 220, 255));
        displayArea.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(15, 15, 15, 15),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 200), 2, true),
                "Resultados / Listados",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Monospaced", Font.BOLD, 16),
                new Color(170, 220, 255)
            )
        ));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(750, 450));
        scrollPane.getViewport().setBackground(new Color(20, 20, 40));

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        Dimension fieldSize = new Dimension(250, 30);

        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);
        Color labelFg = new Color(170, 220, 255);
        Color fieldBg = new Color(60, 60, 100);
        Color fieldFg = Color.WHITE;

        addPersonDialog = new JDialog(this, "Añadir Nueva Persona", true);
        addPersonDialog.setLayout(new BorderLayout(10, 10));
        addPersonDialog.setBackground(new Color(40, 40, 70));
        addPersonDialog.getContentPane().setBackground(new Color(40, 40, 70));
        addPersonDialog.setSize(400, 450);

        JPanel personFormPanel = new JPanel();
        personFormPanel.setLayout(new BoxLayout(personFormPanel, BoxLayout.Y_AXIS));
        personFormPanel.setBackground(new Color(40, 40, 70));
        personFormPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        personNameField = new JTextField();
        personEmailField = new JTextField();
        personTypeComboBox = new JComboBox<>(new String[]{"organizador", "participante"});
        institutionField = new JTextField();
        ageField = new JTextField();
        genderField = new JTextField();
        submitAddPersonButton = new JButton("Confirmar Añadir Persona");

        personFormPanel.add(createStyledLabel("Tipo de Persona:", labelFont, labelFg));
        personTypeComboBox.setFont(labelFont);
        personTypeComboBox.setBackground(fieldBg);
        personTypeComboBox.setForeground(fieldFg);
        personTypeComboBox.setMaximumSize(fieldSize);
        personFormPanel.add(personTypeComboBox);
        personFormPanel.add(Box.createVerticalStrut(5));

        personFormPanel.add(createStyledLabel("Nombre:", labelFont, labelFg));
        personFormPanel.add(createStyledTextField(personNameField, fieldBg, fieldFg, fieldSize));
        personFormPanel.add(Box.createVerticalStrut(5));

        personFormPanel.add(createStyledLabel("Email:", labelFont, labelFg));
        personFormPanel.add(createStyledTextField(personEmailField, fieldBg, fieldFg, fieldSize));
        personFormPanel.add(Box.createVerticalStrut(5));

        institutionLabel = createStyledLabel("Institución (Organizador):", labelFont, labelFg);
        personFormPanel.add(institutionLabel);
        personFormPanel.add(createStyledTextField(institutionField, fieldBg, fieldFg, fieldSize));
        personFormPanel.add(Box.createVerticalStrut(5));

        ageLabel = createStyledLabel("Edad (Participante):", labelFont, labelFg);
        personFormPanel.add(ageLabel);
        personFormPanel.add(createStyledTextField(ageField, fieldBg, fieldFg, fieldSize));
        personFormPanel.add(Box.createVerticalStrut(5));

        genderLabel = createStyledLabel("Género (Participante):", labelFont, labelFg);
        personFormPanel.add(genderLabel);
        personFormPanel.add(createStyledTextField(genderField, fieldBg, fieldFg, fieldSize));
        personFormPanel.add(Box.createVerticalStrut(5));

        addPersonDialog.add(personFormPanel, BorderLayout.CENTER);

        JPanel personDialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        personDialogButtonPanel.setBackground(new Color(40, 40, 70));
        submitAddPersonButton.setFont(buttonFont);
        submitAddPersonButton.setBackground(buttonBg);
        submitAddPersonButton.setForeground(buttonFg);
        submitAddPersonButton.setFocusPainted(false);
        submitAddPersonButton.setBorder(BorderFactory.createLineBorder(new Color(120, 200, 255), 3, true));
        personDialogButtonPanel.add(submitAddPersonButton);
        addPersonDialog.add(personDialogButtonPanel, BorderLayout.SOUTH);

        personTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) personTypeComboBox.getSelectedItem();
                boolean isOrganizer = "organizador".equals(selectedType);
                boolean isParticipante = "participante".equals(selectedType);

                institutionLabel.setVisible(isOrganizer);
                institutionField.setVisible(isOrganizer);
                ageLabel.setVisible(isParticipante);
                ageField.setVisible(isParticipante);
                genderLabel.setVisible(isParticipante);
                genderField.setVisible(isParticipante);
                addPersonDialog.pack();
            }
        });
        personTypeComboBox.setSelectedItem("organizador");
        for (ActionListener al : personTypeComboBox.getActionListeners()) {
            al.actionPerformed(new ActionEvent(personTypeComboBox, ActionEvent.ACTION_PERFORMED, null));
        }

        addActivityDialog = new JDialog(this, "Añadir Nueva Actividad", true);
        addActivityDialog.setLayout(new BorderLayout(10, 10));
        addActivityDialog.setBackground(new Color(40, 40, 70));
        addActivityDialog.getContentPane().setBackground(new Color(40, 40, 70));
        addActivityDialog.setSize(450, 650);

        JPanel activityFormPanel = new JPanel();
        activityFormPanel.setLayout(new BoxLayout(activityFormPanel, BoxLayout.Y_AXIS));
        activityFormPanel.setBackground(new Color(40, 40, 70));
        activityFormPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        activityTypeComboBox = new JComboBox<>(new String[]{"Curso", "Evento", "Taller"});
        activityTitleField = new JTextField();
        activityDescriptionField = new JTextField();
        activityStartDateField = new JTextField("YYYY-MM-DD");
        activityEndDateField = new JTextField("YYYY-MM-DD");
        activityLocationField = new JTextField();
        activityOrganizerIdField = new JTextField();
        activityCapacityField = new JTextField();
        activitySessionsField = new JTextField();
        activityPublicCheckbox = new JCheckBox("Abierto al Público");
        activityMaterialsCheckbox = new JCheckBox("Requiere Materiales");
        submitAddActivityButton = new JButton("Confirmar Añadir Actividad");

        activityFormPanel.add(createStyledLabel("Tipo de Actividad:", labelFont, labelFg));
        activityTypeComboBox.setFont(labelFont);
        activityTypeComboBox.setBackground(fieldBg);
        activityTypeComboBox.setForeground(fieldFg);
        activityTypeComboBox.setMaximumSize(fieldSize);
        activityFormPanel.add(activityTypeComboBox);
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("Título:", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activityTitleField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("Descripción:", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activityDescriptionField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("Fecha Inicio (YYYY-MM-DD):", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activityStartDateField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("Fecha Fin (YYYY-MM-DD):", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activityEndDateField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("Lugar:", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activityLocationField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("ID Organizador:", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activityOrganizerIdField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("Cupo Máximo:", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activityCapacityField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityFormPanel.add(createStyledLabel("Número de Sesiones (Curso):", labelFont, labelFg));
        activityFormPanel.add(createStyledTextField(activitySessionsField, fieldBg, fieldFg, fieldSize));
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityPublicCheckbox.setForeground(labelFg);
        activityPublicCheckbox.setFont(labelFont);
        activityPublicCheckbox.setBackground(new Color(40, 40, 70));
        activityFormPanel.add(activityPublicCheckbox);
        activityFormPanel.add(Box.createVerticalStrut(5));

        activityMaterialsCheckbox.setForeground(labelFg);
        activityMaterialsCheckbox.setFont(labelFont);
        activityMaterialsCheckbox.setBackground(new Color(40, 40, 70));
        activityFormPanel.add(activityMaterialsCheckbox);

        addActivityDialog.add(activityFormPanel, BorderLayout.CENTER);

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dialogButtonPanel.setBackground(new Color(40, 40, 70));
        submitAddActivityButton.setFont(buttonFont);
        submitAddActivityButton.setBackground(buttonBg);
        submitAddActivityButton.setForeground(buttonFg);
        submitAddActivityButton.setFocusPainted(false);
        submitAddActivityButton.setBorder(BorderFactory.createLineBorder(new Color(120, 200, 255), 3, true));
        dialogButtonPanel.add(submitAddActivityButton);
        addActivityDialog.add(dialogButtonPanel, BorderLayout.SOUTH);

        activitySessionsField.setVisible(false);
        activityPublicCheckbox.setVisible(false);
        activityMaterialsCheckbox.setVisible(false);

        activityTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) activityTypeComboBox.getSelectedItem();
                activitySessionsField.setVisible("Curso".equals(selectedType));
                activityPublicCheckbox.setVisible("Evento".equals(selectedType));
                activityMaterialsCheckbox.setVisible("Taller".equals(selectedType));
                addActivityDialog.pack();
            }
        });

        enrollDialog = new JDialog(this, "Inscribir Participante en Actividad", true);
        enrollDialog.setLayout(new BorderLayout(10,10));
        enrollDialog.setBackground(new Color(40, 40, 70));
        enrollDialog.getContentPane().setBackground(new Color(40, 40, 70));
        enrollDialog.setSize(350, 280);

        JPanel enrollFormPanel = new JPanel();
        enrollFormPanel.setLayout(new BoxLayout(enrollFormPanel, BoxLayout.Y_AXIS));
        enrollFormPanel.setBackground(new Color(40, 40, 70));
        enrollFormPanel.setBorder(new EmptyBorder(15,15,15,15));

        enrollParticipantIdField = new JTextField();
        enrollActivityIdField = new JTextField();
        submitEnrollButton = new JButton("Confirmar Inscripción");

        enrollFormPanel.add(createStyledLabel("ID Participante:", labelFont, labelFg));
        enrollFormPanel.add(createStyledTextField(enrollParticipantIdField, fieldBg, fieldFg, fieldSize));
        enrollFormPanel.add(Box.createVerticalStrut(5));

        enrollFormPanel.add(createStyledLabel("ID Actividad:", labelFont, labelFg));
        enrollFormPanel.add(createStyledTextField(enrollActivityIdField, fieldBg, fieldFg, fieldSize));
        enrollFormPanel.add(Box.createVerticalStrut(5));

        enrollDialog.add(enrollFormPanel, BorderLayout.CENTER);

        JPanel enrollButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        enrollButtonPanel.setBackground(new Color(40, 40, 70));
        submitEnrollButton.setFont(buttonFont);
        submitEnrollButton.setBackground(buttonBg);
        submitEnrollButton.setForeground(buttonFg);
        submitEnrollButton.setFocusPainted(false);
        submitEnrollButton.setBorder(BorderFactory.createLineBorder(new Color(120, 200, 255), 3, true));
        enrollButtonPanel.add(submitEnrollButton);
        enrollDialog.add(enrollButtonPanel, BorderLayout.SOUTH);
    }

    private JLabel createStyledLabel(String text, Font font, Color foreground) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(foreground);
        return label;
    }

    private JTextField createStyledTextField(JTextField field, Color background, Color foreground, Dimension size) {
        field.setBackground(background);
        field.setForeground(foreground);
        field.setCaretColor(foreground);
        field.setBorder(new LineBorder(new Color(100, 150, 200), 1));
        field.setPreferredSize(size);
        field.setMaximumSize(size);
        return field;
    }

    private void addEventHandlers() {
        addPersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPersonDialog.setLocationRelativeTo(x.this);
                addPersonDialog.setVisible(true);
            }
        });

        submitAddPersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) personTypeComboBox.getSelectedItem();
                String name = personNameField.getText();
                String email = personEmailField.getText();

                if (type.equals("organizador")) {
                    String institution = institutionField.getText();
                    if (!name.isEmpty() && !email.isEmpty() && !institution.isEmpty()) {
                        sistema.agregarOrganizador(name, email, institution);
                        displayArea.setText("Organizador añadido: " + name);
                    } else {
                        displayArea.setText("Por favor, rellena todos los campos para el Organizador.");
                    }
                } else if (type.equals("participante")) {
                    int age = 0;
                    try {
                        age = Integer.parseInt(ageField.getText());
                    } catch (NumberFormatException ex) {
                        displayArea.setText("Edad inválida. Por favor, ingresa un número.");
                        return;
                    }
                    String gender = genderField.getText();
                    if (!name.isEmpty() && !email.isEmpty() && !gender.isEmpty()) {
                        sistema.agregarParticipante(name, email, age, gender);
                        displayArea.setText("Participante añadido: " + name);
                    } else {
                        displayArea.setText("Por favor, rellena todos los campos para el Participante.");
                    }
                }
                personNameField.setText("");
                personEmailField.setText("");
                institutionField.setText("");
                ageField.setText("");
                genderField.setText("");
                addPersonDialog.dispose();
            }
        });

        viewPersonsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("");
                if (sistema.getPersonasRegistradas().isEmpty()) {
                    displayArea.append("No hay personas registradas.\n");
                } else {
                    displayArea.append("--- Lista de todas las personas ---\n");
                    for (Persona persona : sistema.getPersonasRegistradas()) {
                        displayArea.append("ID: " + persona.getIdPersona() + "\n");
                        displayArea.append("Nombre: " + persona.getNombre() + "\n");
                        displayArea.append("Email: " + persona.getEmail() + "\n");
                        if (persona instanceof Organizador) {
                            displayArea.append("Tipo: Organizador\n");
                            displayArea.append("Institucion: " + ((Organizador) persona).institucion + "\n");
                        } else if (persona instanceof Participante) {
                            Participante participante = (Participante) persona;
                            displayArea.append("Tipo: Participante\n");
                            displayArea.append("Edad: " + participante.edad + "\n");
                            displayArea.append("Genero: " + participante.genero + "\n");
                        }
                        displayArea.append("\n");
                    }
                }
            }
        });

        loadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sistema.cargarDatosDesdeDB();
                displayArea.setText("Datos cargados desde la base de datos.");
            }
        });

        updateDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sistema.actualizarDatosDB();
                displayArea.setText("Base de datos actualizada.");
            }
        });

        addActivityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addActivityDialog.setLocationRelativeTo(x.this);
                addActivityDialog.setVisible(true);
            }
        });

        submitAddActivityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) activityTypeComboBox.getSelectedItem();
                String title = activityTitleField.getText();
                String description = activityDescriptionField.getText();
                String startDateStr = activityStartDateField.getText();
                String endDateStr = activityEndDateField.getText();
                String location = activityLocationField.getText();
                String organizerId = activityOrganizerIdField.getText();
                int capacity = 0;
                try {
                    capacity = Integer.parseInt(activityCapacityField.getText());
                } catch (NumberFormatException ex) {
                    displayArea.setText("Cupo máximo inválido. Ingrese un número.");
                    return;
                }

                Integer numSessions = null;
                Boolean isPublic = null;
                Boolean requiresMaterials = null;

                if ("Curso".equals(type)) {
                    try {
                        numSessions = Integer.parseInt(activitySessionsField.getText());
                    } catch (NumberFormatException ex) {
                        displayArea.setText("Número de sesiones inválido. Ingrese un número.");
                        return;
                    }
                } else if ("Evento".equals(type)) {
                    isPublic = activityPublicCheckbox.isSelected();
                } else if ("Taller".equals(type)) {
                    requiresMaterials = activityMaterialsCheckbox.isSelected();
                }

                if (!title.isEmpty() && !description.isEmpty() && !startDateStr.isEmpty() &&
                    !endDateStr.isEmpty() && !location.isEmpty() && !organizerId.isEmpty()) {

                    if (sistema.agregarActividad(type, title, description, startDateStr, endDateStr, location,
                                                organizerId, capacity, numSessions, isPublic, requiresMaterials)) {
                        displayArea.setText("Actividad '" + title + "' añadida con éxito.");
                        activityTitleField.setText("");
                        activityDescriptionField.setText("");
                        activityStartDateField.setText("YYYY-MM-DD");
                        activityEndDateField.setText("YYYY-MM-DD");
                        activityLocationField.setText("");
                        activityOrganizerIdField.setText("");
                        activityCapacityField.setText("");
                        activitySessionsField.setText("");
                        activityPublicCheckbox.setSelected(false);
                        activityMaterialsCheckbox.setSelected(false);
                        addActivityDialog.dispose();
                    } else {
                        displayArea.setText("Error al añadir actividad. Verifique los datos y el ID del organizador.");
                    }
                } else {
                    displayArea.setText("Por favor, rellena todos los campos obligatorios para la actividad.");
                }
            }
        });

        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollDialog.setLocationRelativeTo(x.this);
                enrollDialog.setVisible(true);
            }
        });

        submitEnrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String participantId = enrollParticipantIdField.getText();
                int activityId = 0;
                try {
                    activityId = Integer.parseInt(enrollActivityIdField.getText());
                } catch (NumberFormatException ex) {
                    displayArea.setText("ID de actividad inválido. Ingrese un número.");
                    return;
                }

                if (!participantId.isEmpty() && activityId != 0) {
                    if (sistema.inscribirParticipanteEnActividad(participantId, activityId)) {
                        displayArea.setText("Participante " + participantId + " inscrito en actividad " + activityId + ".");
                        enrollParticipantIdField.setText("");
                        enrollActivityIdField.setText("");
                        enrollDialog.dispose();
                    } else {
                        displayArea.setText("Error al inscribir. Verifique IDs de participante y actividad, y disponibilidad de cupo.");
                    }
                } else {
                    displayArea.setText("Por favor, rellena ambos campos para la inscripción.");
                }
            }
        });

        viewActivitiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("");
                ArrayList<ActividadComunitaria> actividades = sistema.getActividadesRegistradas();
                if (actividades.isEmpty()) {
                    displayArea.append("No hay actividades registradas.\n");
                } else {
                    displayArea.append("--- Lista de todas las actividades ---\n");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    for (ActividadComunitaria actividad : actividades) {
                        displayArea.append("ID Actividad: " + actividad.getIdActividad() + "\n");
                        displayArea.append("Título: " + actividad.getTitulo() + "\n");
                        displayArea.append("Descripción: " + actividad.descripcion + "\n");
                        displayArea.append("Fecha Inicio: " + sdf.format(actividad.fechaInicio) + "\n");
                        displayArea.append("Fecha Fin: " + sdf.format(actividad.fechaFin) + "\n");
                        displayArea.append("Lugar: " + actividad.lugar + "\n");
                        displayArea.append("Organizador: " + (actividad.organizador != null ? actividad.organizador.getNombre() : "N/A") + " (ID: " + (actividad.organizador != null ? actividad.organizador.getIdPersona() : "N/A") + ")\n");
                        displayArea.append("Cupo Máximo: " + actividad.cupoMaximo + "\n");
                        displayArea.append("Tipo: " + actividad.obtenerTipo() + "\n");

                        if (actividad instanceof Curso) {
                            displayArea.append("Número de Sesiones: " + ((Curso) actividad).numSesiones + "\n");
                        } else if (actividad instanceof Evento) {
                            displayArea.append("Abierto al Público: " + ((Evento) actividad).getEsAbiertoAlPublico() + "\n");
                        } else if (actividad instanceof Taller) {
                            displayArea.append("Requiere Materiales: " + ((Taller) actividad).getRequiereMateriales() + "\n");
                        }
                        displayArea.append("Participantes Inscritos: " + actividad.getListaParticipantes().size() + "\n");
                        displayArea.append("\n");
                    }
                }
            }
        });

        viewEnrollmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText("");
                ArrayList<Inscripcion> inscripciones = sistema.getInscripcionesRealizadas();
                if (inscripciones.isEmpty()) {
                    displayArea.append("No hay inscripciones realizadas.\n");
                } else {
                    displayArea.append("--- Lista de todas las inscripciones ---\n");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (Inscripcion inscripcion : inscripciones) {
                        displayArea.append("ID Inscripción: " + inscripcion.getIdInscripcion() + "\n");
                        displayArea.append("Participante: " + inscripcion.getParticipante().getNombre() + " (ID: " + inscripcion.getParticipante().getIdPersona() + ")\n");
                        displayArea.append("Actividad: " + inscripcion.getActividad().getTitulo() + " (ID: " + inscripcion.getActividad().getIdActividad() + ")\n");
                        displayArea.append("Fecha Inscripción: " + sdf.format(inscripcion.getFechaInscripcion()) + "\n");
                        displayArea.append("\n");
                    }
                }
            }
        });
    }



    @SuppressWarnings("unchecked")
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(x.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(x.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(x.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(x.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new x().setVisible(true);
            }
        });
    }

}
